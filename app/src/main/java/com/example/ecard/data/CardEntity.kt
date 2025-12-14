package com.example.ecard.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    var name: String,
    var company: String?,
    var phone: String?,
    var email: String?,
    // цвет карточки как HEX (например "#FFFFFF")
    var colorHex: String = "#FFFFFF",
    // локальный Uri к логотипу (например content://...) в String
    var logoUri: String? = null,
    // дата создания/обновления
    var updatedAt: Long = System.currentTimeMillis(),
    // Шаблон визитки
    var templateId: String? = null,
    // Категория/тег
    var category: String? = null,
    // Теги (через запятую)
    var tags: String? = null,
    // Статистика просмотров
    var viewCount: Int = 0,
    // Последний просмотр
    var lastViewed: Long? = null,
    // Является ли основной визиткой
    var isPrimary: Boolean = false,
    // ID пользователя, которому принадлежит визитка
    var userId: Long? = null
)
