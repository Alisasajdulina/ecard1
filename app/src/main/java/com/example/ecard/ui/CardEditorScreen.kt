package com.example.ecardnarwhal.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.ecardnarwhal.data.CardEntity
import com.example.ecardnarwhal.data.CardRepository
import com.example.ecardnarwhal.utils.RenderCard
import com.example.ecardnarwhal.utils.ExportUtils
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecard.data.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CardEditorScreen(cardId: Long, onSaved: () -> Unit, vm: CardViewModel = viewModel()) {
    val ctx = LocalContext.current
    val repo = CardRepository(AppDatabase.get(ctx).cardDao())
    var name by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("#FFFFFF") }
    var logoUri by remember { mutableStateOf<Uri?>(null) }

    val pickLogo = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) logoUri = uri
    }

    Column(Modifier
        .fillMaxSize()
        .padding(12.dp)) {
        Card(modifier = Modifier
            .fillMaxWidth()
            .height(180.dp), shape = RoundedCornerShape(8.dp)) {
            Box(
                Modifier
                    .fillMaxSize()
                    .background(
                        androidx.compose.ui.graphics.Color(
                            android.graphics.Color.parseColor(color)
                        )
                    )
            ) {
                Column(Modifier.padding(12.dp)) {
                    Text(
                        name.ifEmpty { "Имя Фамилия" },
                        style = MaterialTheme.typography.titleLarge
                    )
                    Text(company, style = MaterialTheme.typography.bodyMedium)
                    if (logoUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(logoUri),
                            contentDescription = null,
                            modifier = Modifier.size(64.dp)
                        )
                    }
                }
            }
        }
        val keyboardController = LocalSoftwareKeyboardController.current

        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Имя") })
        OutlinedTextField(
            value = company,
            onValueChange = { company = it },
            label = { Text("Компания") },
            keyboardOptions = KeyboardOptions.Default,
            keyboardActions = KeyboardActions(
                onDone = {
                    keyboardController?.hide() // скрыть клавиатуру при нажатии Done
                }
            )
        )
        OutlinedTextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Телефон") })
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })

        Spacer(Modifier.height(8.dp))
        Button(onClick = { pickLogo.launch("image/*") }) { Text("Импортировать логотип") }
        Spacer(Modifier.height(8.dp))

        Row {
            Button(onClick = {
                val entity = CardEntity(
                    name = name,
                    company = company,
                    phone = phone,
                    email = email,
                    colorHex = color,
                    logoUri = logoUri?.toString()
                )
                CoroutineScope(Dispatchers.IO).launch { repo.insert(entity) }
                onSaved()
            }) { Text("Сохранить") }

            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                // Export PNG using MediaStore in background
                val card = RenderCard.CardData(
                    name = name,
                    company = company,
                    phone = phone,
                    email = email,
                    colorHex = color,
                    logoBitmap = null
                )
                CoroutineScope(Dispatchers.IO).launch {
                    val uri =
                        ExportUtils.saveBitmapToGallery(ctx, RenderCard.createBitmap(card), "ecard")
                    // show simple feedback via Toast must be done on main thread - left as exercise
                }
            }) { Text("Экспорт PNG") }

            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                val card = RenderCard.CardData(
                    name = name,
                    company = company,
                    phone = phone,
                    email = email,
                    colorHex = color,
                    logoBitmap = null
                )
                CoroutineScope(Dispatchers.IO).launch {
                    val res = ExportUtils.savePdfToDownloads(
                        ctx,
                        RenderCard.createPdfBytes(card),
                        "ecard"
                    )
                }
            }) { Text("Экспорт PDF") }
        }
    }
}
