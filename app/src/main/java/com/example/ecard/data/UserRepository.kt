package com.example.ecard.data

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.security.MessageDigest

class UserRepository(private val userDao: UserDao) {
    
    suspend fun registerUser(email: String, password: String): Result<Long> {
        return try {
            withContext(Dispatchers.IO) {
                // Проверяем, существует ли пользователь
                val exists = userDao.emailExists(email) > 0
                if (exists) {
                    Result.failure(Exception("Пользователь с таким email уже существует"))
                } else {
                    // Хешируем пароль
                    val passwordHash = hashPassword(password)
                    val user = UserEntity(
                        email = email.lowercase().trim(),
                        passwordHash = passwordHash
                    )
                    val id = userDao.insertUser(user)
                    Result.success(id)
                }
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error registering user", e)
            Result.failure(e)
        }
    }

    suspend fun authenticateUser(email: String, password: String): Result<UserEntity> {
        return try {
            withContext(Dispatchers.IO) {
                val user = userDao.getUserByEmail(email.lowercase().trim())
                if (user == null) {
                    Result.failure(Exception("Пользователь не найден"))
                } else {
                    val passwordHash = hashPassword(password)
                    if (user.passwordHash == passwordHash) {
                        Result.success(user)
                    } else {
                        Result.failure(Exception("Неверный пароль"))
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error authenticating user", e)
            Result.failure(e)
        }
    }

    suspend fun getUserById(id: Long): UserEntity? {
        return withContext(Dispatchers.IO) {
            userDao.getUserById(id)
        }
    }

    suspend fun resetPassword(email: String, newPassword: String): Result<Unit> {
        return try {
            withContext(Dispatchers.IO) {
                val user = userDao.getUserByEmail(email.lowercase().trim())
                if (user == null) {
                    Result.failure(Exception("Пользователь с таким email не найден"))
                } else {
                    val passwordHash = hashPassword(newPassword)
                    userDao.updatePassword(email.lowercase().trim(), passwordHash)
                    Result.success(Unit)
                }
            }
        } catch (e: Exception) {
            Log.e("UserRepository", "Error resetting password", e)
            Result.failure(e)
        }
    }

    private fun hashPassword(password: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(password.toByteArray())
        return hash.joinToString("") { "%02x".format(it) }
    }
}

