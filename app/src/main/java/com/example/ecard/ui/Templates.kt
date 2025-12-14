package com.example.ecard.ui

import androidx.compose.ui.graphics.Color

data class CardTemplate(
    val id: String,
    val name: String,
    val description: String,
    val colorHex: String,
    val gradientColors: List<String>
)

object CardTemplates {
    val templates = listOf(
        CardTemplate(
            id = "gradient_pink",
            name = "Градиент",
            description = "Яркий и современный",
            colorHex = "#FF4D94",
            gradientColors = listOf("#FF4D94", "#E0006E")
        ),
        CardTemplate(
            id = "minimal_white",
            name = "Минимализм",
            description = "Простой и чистый",
            colorHex = "#FFFFFF",
            gradientColors = listOf("#FFFFFF", "#F5F5F5")
        ),
        CardTemplate(
            id = "bright_blue",
            name = "Яркий",
            description = "Смелый и выразительный",
            colorHex = "#2196F3",
            gradientColors = listOf("#2196F3", "#1976D2")
        ),
        CardTemplate(
            id = "elegant_purple",
            name = "Элегантный",
            description = "Классический стиль",
            colorHex = "#9C27B0",
            gradientColors = listOf("#9C27B0", "#7B1FA2")
        ),
        CardTemplate(
            id = "warm_orange",
            name = "Теплый",
            description = "Дружелюбный и приветливый",
            colorHex = "#FF9800",
            gradientColors = listOf("#FF9800", "#F57C00")
        ),
        CardTemplate(
            id = "cool_green",
            name = "Свежий",
            description = "Природный и спокойный",
            colorHex = "#4CAF50",
            gradientColors = listOf("#4CAF50", "#388E3C")
        )
    )
    
    fun getTemplateById(id: String?): CardTemplate? {
        return templates.find { it.id == id }
    }
}

