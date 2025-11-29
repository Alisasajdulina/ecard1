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
    var updatedAt: Long = System.currentTimeMillis()
)
