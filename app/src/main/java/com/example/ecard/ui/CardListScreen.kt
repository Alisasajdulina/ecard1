package com.example.ecard.ui

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecard.data.CardEntity
import kotlinx.coroutines.launch
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.ui.graphics.Brush
import com.example.ecard.theme.LightPink
import com.example.ecard.theme.Pink
import com.example.ecard.theme.PinkDark
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Settings
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.graphics.Color
<<<<<<< HEAD
import androidx.compose.ui.platform.LocalContext
import com.example.ecard.utils.ExportUtils
import com.example.ecard.utils.ImagePicker
import com.example.ecard.utils.RenderCard
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.launch
=======
import java.text.SimpleDateFormat
import java.util.*
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(
    viewModel: CardViewModel,
    onCreateNew: () -> Unit,
    onEdit: (Long) -> Unit,
    onShowQR: (Long) -> Unit,
    onSettings: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val scope = rememberCoroutineScope()
<<<<<<< HEAD
    val snackbarHostState = remember { SnackbarHostState() }
=======
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
    var showSearchBar by remember { mutableStateOf(false) }
    var categories by remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(Unit) {
        categories = viewModel.getAllCategories()
    }

    Scaffold(
        topBar = {
<<<<<<< HEAD

=======
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
            CenterAlignedTopAppBar(
                title = { Text("Ваши визитки") },
                actions = {
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(
                            if (showSearchBar) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "Поиск"
                        )
                    }
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Настройки")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNew,
                containerColor = Pink,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Создать визитку")
            }
<<<<<<< HEAD
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
=======
        }
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(LightPink)
        ) {
            if (showSearchBar) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { viewModel.setSearchQuery(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    placeholder = { Text("Поиск по имени, компании, телефону...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { viewModel.setSearchQuery("") }) {
                                Icon(Icons.Default.Close, contentDescription = "Очистить")
                            }
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = { })
                )
            }
            
            // Фильтры по категориям
            if (categories.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedCategory == null,
                        onClick = { viewModel.setCategory(null) },
                        label = { Text("Все") }
                    )
                    categories.forEach { category ->
                        FilterChip(
                            selected = selectedCategory == category,
                            onClick = { viewModel.setCategory(category) },
                            label = { Text(category) }
                        )
                    }
                }
                Spacer(Modifier.height(8.dp))
            }
            
            Box(modifier = Modifier.fillMaxSize()) {
                when (uiState) {
                    is UiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                    is UiState.Error -> Text((uiState as UiState.Error).message, color = MaterialTheme.colorScheme.error, modifier = Modifier.align(Alignment.Center))
                    is UiState.Success -> {
                        val list = (uiState as UiState.Success).cards
                        if (list.isEmpty()) {
                            Text("Пусто — создайте первую карточку", modifier = Modifier.align(Alignment.Center))
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
                                items(list) { card ->
                                    CardItem(
                                        card = card,
                                        onClick = { onEdit(card.id) },
                                        onDelete = {
<<<<<<< HEAD
                                            viewModel.delete(card) { result ->
                                                scope.launch {
                                                    result.fold(
                                                        onSuccess = {
                                                            snackbarHostState.showSnackbar("Визитка удалена")
                                                        },
                                                        onFailure = {
                                                            snackbarHostState.showSnackbar("Ошибка удаления")
                                                        }
                                                    )
                                                }
                                            }
                                        },
                                        onShowQR = { onShowQR(card.id) },
                                        snackbarHostState = snackbarHostState
=======
                                            scope.launch { viewModel.delete(card) {} }
                                        },
                                        onShowQR = { onShowQR(card.id) }
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
                                    )
                                    Spacer(Modifier.height(8.dp))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
<<<<<<< HEAD
fun CardItem(
    card: CardEntity, 
    onClick: () -> Unit, 
    onDelete: () -> Unit, 
    onShowQR: () -> Unit,
    snackbarHostState: SnackbarHostState? = null
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
=======
fun CardItem(card: CardEntity, onClick: () -> Unit, onDelete: () -> Unit, onShowQR: () -> Unit) {
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
    val df = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val created = remember(card.updatedAt) { df.format(Date(card.updatedAt)) }
    val bgColor = remember(card.colorHex) {
        runCatching { Color(android.graphics.Color.parseColor(card.colorHex)) }.getOrElse { Pink }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.6f))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .clip(RoundedCornerShape(16.dp)),
            color = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                bgColor,
                                PinkDark
                            )
                        )
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Text(card.name, style = MaterialTheme.typography.titleLarge, color = Color.White, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Spacer(Modifier.height(4.dp))
                    Text(card.company ?: "Компания не указана", style = MaterialTheme.typography.bodyMedium, color = Color.White.copy(alpha = 0.9f))
                    Spacer(Modifier.height(12.dp))
                    Text(card.email ?: "", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.85f))
                    Text(card.phone ?: "", style = MaterialTheme.typography.bodySmall, color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
        Spacer(Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            Button(
<<<<<<< HEAD
                onClick = {
                    scope.launch {
                        val vcard = ExportUtils.createVCard(
                            card.name,
                            card.company,
                            card.phone,
                            card.email
                        )
                        val saved = ExportUtils.saveVCardToDownloads(context, vcard, card.name)
                        snackbarHostState?.let {
                            scope.launch {
                                it.showSnackbar(
                                    if (saved != null) "vCard сохранен в Downloads" else "Ошибка сохранения"
                                )
                            }
                        }
                    }
                },
=======
                onClick = { /* TODO export vCard */ },
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = Pink)
            ) {
                Icon(Icons.Default.Download, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary)
                Spacer(Modifier.width(6.dp))
                Text("Скачать vCard", color = MaterialTheme.colorScheme.onPrimary)
            }
            OutlinedButton(
                onClick = onShowQR,
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.QrCode, contentDescription = "QR-код")
            }
            OutlinedButton(
<<<<<<< HEAD
                onClick = {
                    scope.launch {
                        try {
                            val photoBitmap = card.logoUri?.let {
                                ImagePicker.getBitmapFromUriPath(context, it)
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
                        } catch (e: Exception) {
                            // Ошибка уже обработана в ExportUtils
                        }
                    }
                },
=======
                onClick = { /* TODO share */ },
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.Default.Share, contentDescription = "Поделиться")
            }
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
        Spacer(Modifier.height(8.dp))
        Text("Создано: $created", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
    }
}
