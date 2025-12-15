package com.example.ecard.ui

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ecard.data.CardEntity
import com.example.ecard.theme.LightPink
import com.example.ecard.theme.Pink
import com.example.ecard.theme.PinkDark
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

/* -----------------------------
   ✅ ЯВНОЕ СОСТОЯНИЕ UI
-------------------------------- */
sealed class UiState {
    object Loading : UiState()
    data class Success(val cards: List<CardEntity>) : UiState()
    data class Error(val message: String) : UiState()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardListScreen(
    viewModel: CardViewModel,
    onCreateNew: () -> Unit,
    onEdit: (Long) -> Unit,
    onShowQR: (Long) -> Unit,
    onSettings: () -> Unit = {}
) {
    val uiState: UiState by viewModel.uiState.collectAsState()
    val searchQuery: String by viewModel.searchQuery.collectAsState()
    val selectedCategory: String? by viewModel.selectedCategory.collectAsState()

    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showSearchBar by remember { mutableStateOf(false) }

    val cards = (uiState as? UiState.Success)?.cards ?: emptyList()

    val categories = remember(cards) {
        cards.mapNotNull { it.category }.distinct()
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Ваши визитки") },
                actions = {
                    IconButton(onClick = { showSearchBar = !showSearchBar }) {
                        Icon(
                            if (showSearchBar) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = null)
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateNew,
                containerColor = Pink
            ) {
                Icon(Icons.Default.Add, null)
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
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
                    placeholder = { Text("Поиск...") },
                    leadingIcon = { Icon(Icons.Default.Search, null) },
                    singleLine = true
                )
            }

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
            }

            Spacer(Modifier.height(8.dp))

            when (uiState) {
                is UiState.Loading -> {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(Modifier.align(Alignment.Center))
                    }
                }

                is UiState.Error -> {
                    Box(Modifier.fillMaxSize()) {
                        Text(
                            (uiState as UiState.Error).message,
                            modifier = Modifier.align(Alignment.Center),
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                is UiState.Success -> {
                    if (cards.isEmpty()) {
                        Box(Modifier.fillMaxSize()) {
                            Text(
                                "Пусто — создайте первую визитку",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = cards,
                                key = { it.id ?: 0L }
                            ) { card: CardEntity ->
                                CardItem(
                                    card = card,
                                    onClick = { onEdit(card.id ?: 0L) },
                                    onDelete = {
                                        scope.launch {
                                            val success = viewModel.deleteCard(card)
                                            snackbarHostState.showSnackbar(
                                                if (success)
                                                    "Визитка удалена"
                                                else
                                                    "Ошибка удаления"
                                            )
                                        }
                                    },
                                    onShowQR = { onShowQR(card.id ?: 0L) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardItem(
    card: CardEntity,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    onShowQR: () -> Unit
) {
    val df = remember { SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()) }
    val created = df.format(Date(card.updatedAt))

    val bgColor = remember(card.colorHex) {
        runCatching {
            Color(android.graphics.Color.parseColor(card.colorHex))
        }.getOrElse { Pink }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White.copy(alpha = 0.9f))
            .clickable(onClick = onClick)
            .padding(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Brush.linearGradient(listOf(bgColor, PinkDark)))
                .padding(16.dp)
        ) {
            Column {
                Text(card.name, style = MaterialTheme.typography.titleLarge, color = Color.White)
                card.company?.let { Text(it, color = Color.White) }
                card.phone?.let { Text(it, color = Color.White) }
            }
        }

        Spacer(Modifier.height(6.dp))

        Row {
            IconButton(onClick = onShowQR) {
                Icon(Icons.Default.QrCode, null)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error)
            }
        }

        Text("Создано: $created", style = MaterialTheme.typography.bodySmall)
    }
}