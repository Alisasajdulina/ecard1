package com.example.ecard.utils

import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.tasks.await
import java.util.regex.Pattern

data class ScannedCardData(
    val name: String = "",
    val company: String = "",
    val phone: String = "",
    val email: String = ""
)

object CardScanner {
    private val textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    suspend fun scanFromBitmap(bitmap: Bitmap): Result<ScannedCardData> {
        return try {
            val image = InputImage.fromBitmap(bitmap, 0)
            val result = textRecognizer.process(image).await()
            
            if (result.text.isBlank()) {
                Result.failure(Exception("Не удалось распознать текст на изображении"))
            } else {
                val scannedData = extractCardData(result.text)
                Result.success(scannedData)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun extractCardData(text: String): ScannedCardData {
        val lines = text.lines().map { it.trim() }.filter { it.isNotBlank() }
        
        var name = ""
        var company = ""
        var phone = ""
        var email = ""

        // Извлекаем email
        val emailPattern = Pattern.compile(
            "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}",
            Pattern.CASE_INSENSITIVE
        )
        val emailMatcher = emailPattern.matcher(text)
        if (emailMatcher.find()) {
            email = emailMatcher.group()
        }

        // Извлекаем телефон
        val phonePatterns = listOf(
            Pattern.compile("\\+?[0-9]{1,3}?[-.\\s]?\\(?[0-9]{1,4}\\)?[-.\\s]?[0-9]{1,4}[-.\\s]?[0-9]{1,9}"),
            Pattern.compile("\\+?[0-9]{10,15}"),
            Pattern.compile("\\(?[0-9]{3}\\)?[-.\\s]?[0-9]{3}[-.\\s]?[0-9]{4}")
        )
        
        for (pattern in phonePatterns) {
            val phoneMatcher = pattern.matcher(text)
            if (phoneMatcher.find()) {
                phone = phoneMatcher.group().replace(Regex("[^0-9+]"), "")
                if (phone.length >= 10) break
            }
        }

        // Извлекаем имя и компанию
        // Обычно имя - это первая или вторая строка, если она не содержит email/телефон
        val filteredLines = lines.filter { line ->
            !emailPattern.matcher(line).find() &&
            !phonePatterns.any { it.matcher(line).find() } &&
            !line.matches(Regex(".*[0-9]{5,}.*")) && // не содержит длинные числа
            line.length > 2 &&
            !line.contains("@") &&
            !line.contains("www.") &&
            !line.contains("http")
        }

        when {
            filteredLines.isEmpty() -> {
                // Если нет подходящих строк, берем первую непустую
                name = lines.firstOrNull { it.isNotBlank() } ?: ""
            }
            filteredLines.size == 1 -> {
                name = filteredLines[0]
            }
            else -> {
                // Первая строка - обычно имя, вторая - компания
                name = filteredLines[0]
                company = filteredLines[1]
            }
        }

        // Если имя слишком длинное, возможно это компания
        if (name.length > 30) {
            company = name
            name = filteredLines.getOrNull(1) ?: ""
        }

        // Очистка данных
        name = name.trim().take(100)
        company = company.trim().take(100)
        phone = phone.trim().take(20)
        email = email.trim().lowercase().take(100)

        return ScannedCardData(
            name = name,
            company = company,
            phone = phone,
            email = email
        )
    }

    fun close() {
        textRecognizer.close()
    }
}

