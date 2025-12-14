package com.example.ecard.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecard.data.AppDatabase
import com.example.ecard.data.UserRepository
import com.example.ecard.utils.AuthManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    data class Success(val userId: Long) : AuthState()
    data class Error(val message: String) : AuthState()
}

class AuthViewModel(application: Application) : AndroidViewModel(application) {
    private val userRepository: UserRepository
    private val authManager: AuthManager

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    init {
        val dao = AppDatabase.getInstance(application).userDao()
        userRepository = UserRepository(dao)
        authManager = AuthManager(application)
    }

    fun register(email: String, password: String, onResult: (Result<Long>) -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = userRepository.registerUser(email, password)
                result.fold(
                    onSuccess = { userId ->
                        authManager.saveUserSession(userId, email)
                        _authState.value = AuthState.Success(userId)
                        onResult(Result.success(userId))
                    },
                    onFailure = { error ->
                        _authState.value = AuthState.Error(error.message ?: "Ошибка регистрации")
                        onResult(Result.failure(error))
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Неизвестная ошибка")
                onResult(Result.failure(e))
            }
        }
    }

    fun login(email: String, password: String, onResult: (Result<Long>) -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = userRepository.authenticateUser(email, password)
                result.fold(
                    onSuccess = { user ->
                        authManager.saveUserSession(user.id, user.email)
                        _authState.value = AuthState.Success(user.id)
                        onResult(Result.success(user.id))
                    },
                    onFailure = { error ->
                        _authState.value = AuthState.Error(error.message ?: "Ошибка входа")
                        onResult(Result.failure(error))
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Неизвестная ошибка")
                onResult(Result.failure(e))
            }
        }
    }

    fun isLoggedIn(): Boolean {
        return authManager.isLoggedIn()
    }

    fun logout() {
        authManager.logout()
        _authState.value = AuthState.Idle
    }

    fun resetPassword(email: String, newPassword: String, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            try {
                val result = userRepository.resetPassword(email, newPassword)
                result.fold(
                    onSuccess = {
                        _authState.value = AuthState.Success(0) // Используем 0 как индикатор успешного сброса
                        onResult(Result.success(Unit))
                    },
                    onFailure = { error ->
                        _authState.value = AuthState.Error(error.message ?: "Ошибка сброса пароля")
                        onResult(Result.failure(error))
                    }
                )
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Неизвестная ошибка")
                onResult(Result.failure(e))
            }
        }
    }
}

