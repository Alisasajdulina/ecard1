package com.example.ecard.ui

import android.graphics.Color as AndroidColor
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.example.ecard.data.CardEntity
import com.example.ecard.theme.Pink
import com.example.ecard.utils.ExportUtils
import com.example.ecard.utils.RenderCard
import kotlinx.coroutines.launch
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.Badge
import com.example.ecard.theme.PinkDark
import com.example.ecard.ui.ColorChip
import com.example.ecard.utils.ImagePicker
import com.example.ecard.utils.rememberImagePickerLauncher
import com.example.ecard.utils.ContactUtils
import android.net.Uri
import androidx.compose.material.icons.filled.Contacts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.BorderStroke
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.ui.text.font.FontWeight
import com.example.ecard.utils.CardScanner
import android.graphics.Bitmap

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardEditorScreen(
    viewModel: CardViewModel,
    cardId: Long,
    initialTemplateId: String? = null,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current

    var editing by remember { mutableStateOf<CardEntity?>(null) }

    val name = rememberSaveable(cardId) { mutableStateOf("") }
    val company = rememberSaveable(cardId) { mutableStateOf("") }
    val phone = rememberSaveable(cardId) { mutableStateOf("") }
    val email = rememberSaveable(cardId) { mutableStateOf("") }
    val color = rememberSaveable(cardId) { mutableStateOf("#FFE8F2") }
    val photoUriString = rememberSaveable(cardId) { mutableStateOf<String?>(null) }
    val photoUri = remember(photoUriString.value) { 
        photoUriString.value?.let { Uri.parse(it) }
    }
    val templateId = rememberSaveable(cardId) { mutableStateOf<String?>(null) }
    val category = rememberSaveable(cardId) { mutableStateOf("") }
    val tags = rememberSaveable(cardId) { mutableStateOf("") }
    val palette = listOf("#FF4D94", "#FF6FA8", "#FFA3C8", "#FFD6E9", "#FFE8F2", "#FFB347", "#F4C430")

    var isScanning by remember { mutableStateOf(false) }
    
    val imagePickerLauncher = rememberImagePickerLauncher { uri ->
        if (uri != null) {
            photoUriString.value = uri.toString()
        }
    }
    
    val scanImageLauncher = rememberImagePickerLauncher { uri ->
        if (uri != null) {
            scope.launch {
                isScanning = true
                try {
                    val bitmap = ImagePicker.getBitmapFromUri(context, uri)
                    if (bitmap != null) {
                        val scanResult = CardScanner.scanFromBitmap(bitmap)
                        scanResult.fold(
                            onSuccess = { scannedData ->
                                // Заполняем поля только если они пустые
                                if (name.value.isBlank()) name.value = scannedData.name
                                if (company.value.isBlank()) company.value = scannedData.company
                                if (phone.value.isBlank()) phone.value = scannedData.phone
                                if (email.value.isBlank()) email.value = scannedData.email
                                
                                // Сохраняем фото визитки
                                photoUriString.value = uri.toString()
                                
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Данные распознаны: ${scannedData.name.ifBlank { "не найдено" }}"
                                    )
                                }
                            },
                            onFailure = { error ->
                                scope.launch {
                                    snackbarHostState.showSnackbar("Ошибка сканирования: ${error.message}")
                                }
                            }
                        )
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Не удалось загрузить изображение")
                        }
                    }
                } catch (e: Exception) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Ошибка: ${e.message}")
                    }
                } finally {
                    isScanning = false
                }
            }
        }
    }

    LaunchedEffect(cardId, initialTemplateId) {
        val loaded = if (cardId != 0L) viewModel.getById(cardId) else CardEntity(name = "", company = null, phone = null, email = null)
        editing = loaded
        loaded?.let {
            name.value = it.name
            company.value = it.company.orEmpty()
            phone.value = it.phone.orEmpty()
            email.value = it.email.orEmpty()
            color.value = it.colorHex
            photoUriString.value = it.logoUri
            templateId.value = it.templateId
            category.value = it.category.orEmpty()
            tags.value = it.tags.orEmpty()
        } ?: run {
            // Если это новая визитка и передан шаблон, устанавливаем его
            initialTemplateId?.let {
                templateId.value = it
            }
        }
    }

    val previewColor = remember(color.value) {
        runCatching {
            androidx.compose.ui.graphics.Color(AndroidColor.parseColor(color.value))
        }.getOrElse { Pink }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(if (cardId == 0L) "Новая визитка" else "Редактирование") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Назад"
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                colors = CardDefaults.cardColors(containerColor = previewColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(name.value.ifBlank { "ФИО" }, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onPrimary)
                            Text(company.value.ifBlank { "Компания" }, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.9f))
                            Spacer(Modifier.height(8.dp))
                            Text(phone.value.ifBlank { "+7 (___) ___-__-__" }, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary)
                            Text(email.value.ifBlank { "email@example.com" }, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f))
                        }
                        photoUri?.let { uri ->
                            Image(
                                painter = rememberAsyncImagePainter(uri),
                                contentDescription = "Фото профиля",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }

            // Секция сканирования визитки
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Pink.copy(alpha = 0.1f)
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.QrCode,
                            contentDescription = null,
                            tint = PinkDark,
                            modifier = Modifier.size(24.dp)
                        )
                        Text(
                            "Сканировать визитку",
                            style = MaterialTheme.typography.titleSmall,
                            color = PinkDark,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        "Выберите фото визитки для автоматического заполнения полей",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Button(
                        onClick = { scanImageLauncher.launch("image/*") },
                        enabled = !isScanning,
                        colors = ButtonDefaults.buttonColors(containerColor = Pink)
                    ) {
                        if (isScanning) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp
                            )
                            Spacer(Modifier.width(8.dp))
                            Text("Сканирование...", color = MaterialTheme.colorScheme.onPrimary)
                        } else {
                            Icon(Icons.Default.QrCode, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Выбрать фото для сканирования")
                        }
                    }
                }
            }

            // Секция загрузки фото
            Text("Фото профиля", style = MaterialTheme.typography.titleSmall, color = PinkDark)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                photoUri?.let { uri ->
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Фото",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )
                } ?: Box(
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { imagePickerLauncher.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = "Добавить фото", tint = PinkDark, modifier = Modifier.size(40.dp))
                }
                OutlinedButton(onClick = { imagePickerLauncher.launch("image/*") }) {
                    Icon(Icons.Default.AddPhotoAlternate, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Выбрать фото")
                }
            }

            OutlinedTextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Имя") },
                leadingIcon = { Icon(Icons.Default.Badge, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            )
            OutlinedTextField(
                value = company.value,
                onValueChange = { company.value = it },
                label = { Text("Компания / Должность") },
                leadingIcon = { Icon(Icons.Default.BusinessCenter, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            )
            OutlinedTextField(
                value = phone.value,
                onValueChange = { phone.value = it },
                label = { Text("Телефон") },
                leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            )
            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            )

            Text("Стиль визитки", style = MaterialTheme.typography.titleSmall, color = PinkDark)
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(Icons.Default.ColorLens, contentDescription = null, tint = PinkDark)
                palette.forEach { hex ->
                    ColorChip(
                        selected = color.value.equals(hex, ignoreCase = true),
                        color = Color(android.graphics.Color.parseColor(hex)),
                        onClick = { color.value = hex }
                    )
                }
            }
            OutlinedTextField(
                value = color.value,
                onValueChange = { color.value = it },
                label = { Text("Цвет (HEX)") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large
            )

            OutlinedTextField(
                value = category.value,
                onValueChange = { category.value = it },
                label = { Text("Категория") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                placeholder = { Text("Работа, Личное, Друзья...") }
            )

            OutlinedTextField(
                value = tags.value,
                onValueChange = { tags.value = it },
                label = { Text("Теги (через запятую)") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                placeholder = { Text("важное, клиент, встреча...") }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Button(
                    onClick = {
                        val card = editing?.copy(
                            name = name.value.trim(),
                            company = company.value.trim().ifEmpty { null },
                            phone = phone.value.trim().ifEmpty { null },
                            email = email.value.trim().ifEmpty { null },
                            colorHex = color.value.trim().ifEmpty { "#FFE8F2" },
                            logoUri = photoUriString.value,
                            templateId = templateId.value,
                            category = category.value.trim().ifEmpty { null },
                            tags = tags.value.trim().ifEmpty { null }
                        ) ?: CardEntity(
                            name = name.value.trim(),
                            company = company.value.trim().ifEmpty { null },
                            phone = phone.value.trim().ifEmpty { null },
                            email = email.value.trim().ifEmpty { null },
                            colorHex = color.value.trim().ifEmpty { "#FFE8F2" },
                            logoUri = photoUriString.value,
                            templateId = templateId.value,
                            category = category.value.trim().ifEmpty { null },
                            tags = tags.value.trim().ifEmpty { null }
                        )

                        scope.launch {
                            viewModel.createOrUpdate(card) { result ->
                                result.fold(onSuccess = {
                                    scope.launch { snackbarHostState.showSnackbar("Сохранено") }
                                    onBack()
                                }, onFailure = {
                                    scope.launch { snackbarHostState.showSnackbar("Ошибка: ${it.localizedMessage}") }
                                })
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Pink)
                ) { Text("Сохранить", color = MaterialTheme.colorScheme.onPrimary) }

                OutlinedButton(
                    onClick = {
                        val photoBitmap = photoUri?.let {
                            ImagePicker.getBitmapFromUri(context, it)
                        }
                        val card = RenderCard.CardData(
                            name = name.value.ifBlank { "E-Card" },
                            company = company.value.ifBlank { "" },
                            phone = phone.value.ifBlank { "" },
                            email = email.value.ifBlank { "" },
                            colorHex = color.value.trim().ifEmpty { "#FFE8F2" },
                            logoBitmap = photoBitmap
                        )
                        val bmp = RenderCard.createBitmap(card)
                        val saved = ExportUtils.saveBitmapToGallery(context, bmp, "ecard")
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                saved?.let { "PNG сохранен в галерею" } ?: "Не удалось сохранить"
                            )
                        }
                    }
                ) { Text("Экспорт PNG") }

                OutlinedButton(
                    onClick = {
                        val vcard = ExportUtils.createVCard(
                            name.value.ifBlank { "E-Card" },
                            company.value.ifBlank { null },
                            phone.value.ifBlank { null },
                            email.value.ifBlank { null }
                        )
                        val saved = ExportUtils.saveVCardToDownloads(context, vcard, "ecard")
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                saved?.let { "vCard сохранен" } ?: "Не удалось сохранить"
                            )
                        }
                    }
                ) { Text("Экспорт vCard") }

                OutlinedButton(
                    onClick = {
                        val card = editing?.copy(
                            name = name.value.trim(),
                            company = company.value.trim().ifEmpty { null },
                            phone = phone.value.trim().ifEmpty { null },
                            email = email.value.trim().ifEmpty { null }
                        ) ?: CardEntity(
                            name = name.value.trim(),
                            company = company.value.trim().ifEmpty { null },
                            phone = phone.value.trim().ifEmpty { null },
                            email = email.value.trim().ifEmpty { null }
                        )
                        val saved = ContactUtils.saveToContacts(context, card)
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                if (saved) "Сохранено в контакты" else "Ошибка сохранения"
                            )
                        }
                    }
                ) {
                    Icon(Icons.Default.Contacts, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("В контакты")
                }
            }
        }
    }
}