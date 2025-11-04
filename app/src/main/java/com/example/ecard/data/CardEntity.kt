
package com.example.ecardnarwhal.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var name: String = "",
    var company: String = "",
    var phone: String = "",
    var email: String = "",
    var colorHex: String? = null,
    var logoUri: String? = null,
    var template: Int = 0
)
