package com.example.ecard.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import com.example.ecard.data.CardEntity
import com.example.ecard.utils.ExportUtils
import com.example.ecard.utils.RenderCard
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import android.graphics.Color as AndroidColor
import androidx.compose.ui.platform.LocalContext

<<<<<<< HEAD
@OptIn(ExperimentalMaterial3Api::class)
=======
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
@Composable
fun QRCodeScreen(
    card: CardEntity,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    
    val qrBitmap = remember(card) {
        val vcard = ExportUtils.createVCard(
            card.name,
            card.company,
            card.phone,
            card.email
        )
        generateQRCode(vcard, 400, 400)
    }
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("QR-код визитки") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        val photoBitmap = card.logoUri?.let { 
                            com.example.ecard.utils.ImagePicker.getBitmapFromUriPath(context, it) 
                        }
                        val cardData = RenderCard.CardData(
                            name = card.name,
                            company = card.company ?: "",
                            phone = card.phone ?: "",
                            email = card.email ?: "",
                            colorHex = card.colorHex,
                            logoBitmap = photoBitmap
                        )
                        val bmp = RenderCard.createBitmap(cardData)
                        ExportUtils.shareBitmap(context, bmp, card.name)
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Поделиться")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .size(300.dp)
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    qrBitmap?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "QR Code",
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }
            
            Spacer(Modifier.height(24.dp))
            
            Text(
                "Отсканируйте QR-код для сохранения контакта",
                style = MaterialTheme.typography.bodyLarge
            )
            
            Spacer(Modifier.height(16.dp))
            
            Button(
                onClick = {
                    val vcard = ExportUtils.createVCard(
                        card.name,
                        card.company,
                        card.phone,
                        card.email
                    )
                    ExportUtils.shareVCard(context, vcard, card.name)
                }
            ) {
                Icon(Icons.Default.Share, contentDescription = null)
                Spacer(Modifier.width(8.dp))
                Text("Поделиться vCard")
            }
        }
    }
}

fun generateQRCode(text: String, width: Int, height: Int): Bitmap? {
    return try {
        val matrix = MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height)
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (matrix.get(x, y)) AndroidColor.BLACK else AndroidColor.WHITE)
            }
        }
        bmp
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

