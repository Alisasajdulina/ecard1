
package com.example.ecard.utils

import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.graphics.Bitmap
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter

object RenderCard {
    data class CardData(
        val name: String,
        val company: String,
        val phone: String,
        val email: String,
        val colorHex: String = "#FFFFFF",
        val logoBitmap: Bitmap? = null
    )
    fun createBitmap(card: CardData, width: Int = 1200, height: Int = 700): Bitmap {
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        val paint = Paint().apply { style = Paint.Style.FILL; color = Color.parseColor(card.colorHex) }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)

        // Определяем цвет текста в зависимости от яркости фона
        val bgColor = Color.parseColor(card.colorHex)
        val brightness = (Color.red(bgColor) * 0.299 + Color.green(bgColor) * 0.587 + Color.blue(bgColor) * 0.114)
        val textColor = if (brightness > 128) Color.BLACK else Color.WHITE
        
        val textPaint = Paint().apply { 
            color = textColor
            textSize = 64f
            isAntiAlias = true
            typeface = android.graphics.Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        }
        var yPos = 120f
        canvas.drawText(card.name, 40f, yPos, textPaint)
        
        if (card.company.isNotBlank()) {
            textPaint.textSize = 48f
            textPaint.typeface = android.graphics.Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            yPos += 70f
            canvas.drawText(card.company, 40f, yPos, textPaint)
        }
        
        textPaint.textSize = 40f
        yPos += 60f
        if (card.phone.isNotBlank()) {
            canvas.drawText(card.phone, 40f, yPos, textPaint)
            yPos += 50f
        }
        if (card.email.isNotBlank()) {
            canvas.drawText(card.email, 40f, yPos, textPaint)
        }

        card.logoBitmap?.let {
            val logoSize = 200
            val logo = Bitmap.createScaledBitmap(it, logoSize, logoSize, true)
            val xPos = (width - logoSize - 40).toFloat()
            val yPosLogo = 40f
            canvas.drawBitmap(logo, xPos, yPosLogo, Paint().apply { isAntiAlias = true })
        }

        val qrText = "${card.name};${card.company};${card.phone};${card.email}"
        val qr = generateQr(qrText, 300, 300)
        canvas.drawBitmap(qr, (width - 360).toFloat(), (height - 360).toFloat(), null)

        return bmp
    }

    fun createPdfBytes(card: CardData, pageWidth: Int = 595, pageHeight: Int = 842): ByteArray {
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas
        val paint = Paint().apply { style = Paint.Style.FILL; color = Color.parseColor(card.colorHex) }
        canvas.drawRect(0f, 0f, pageWidth.toFloat(), pageHeight.toFloat(), paint)

        val textPaint = Paint().apply { color = Color.BLACK; textSize = 14f; isAntiAlias = true }
        canvas.drawText(card.name, 40f, 80f, textPaint)
        textPaint.textSize = 12f
        canvas.drawText(card.company, 40f, 110f, textPaint)
        canvas.drawText(card.phone, 40f, 140f, textPaint)
        canvas.drawText(card.email, 40f, 170f, textPaint)

        card.logoBitmap?.let {
            val logo = Bitmap.createScaledBitmap(it, 120, 120, true)
            canvas.drawBitmap(logo, (pageWidth - 160).toFloat(), 40f, null)
        }

        val qrText = "${card.name};${card.company};${card.phone};${card.email}"
        val qr = generateQr(qrText, 200, 200)
        canvas.drawBitmap(qr, (pageWidth - 260).toFloat(), (pageHeight - 260).toFloat(), null)

        document.finishPage(page)
        val bos = java.io.ByteArrayOutputStream()
        document.writeTo(bos)
        document.close()
        return bos.toByteArray()
    }

    private fun generateQr(text: String, w: Int, h: Int): Bitmap {
        val matrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, w, h)
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        for (x in 0 until w) for (y in 0 until h) bmp.setPixel(x, y, if (matrix.get(x, y)) Color.BLACK else Color.WHITE)
        return bmp
    }
}
