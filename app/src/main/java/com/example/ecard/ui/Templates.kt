package com.example.ecard.ui

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
        )
    )

    fun getTemplateById(id: String?): CardTemplate? =
        templates.find { it.id == id }
}
