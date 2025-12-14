package com.example.ecard.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val email: String,
    val passwordHash: String, // Хеш пароля
    val createdAt: Long = System.currentTimeMillis()
)

