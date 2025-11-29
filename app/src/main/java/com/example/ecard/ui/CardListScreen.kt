package com.example.ecard.ui

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.ecard.data.CardEntity
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.Alignment
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun CardListScreen(
    viewModel: CardViewModel,
    onCreateNew: () -> Unit,
    onEdit: (Long) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = { TopAppBar(title = { Text("E-Card") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreateNew) { Text("+") }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding).fillMaxSize()) {
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
                                CardItem(card = card, onClick = { onEdit(card.id) }, onDelete = {
                                    scope.launch { viewModel.delete(card) {} }
                                })
                                Spacer(Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardItem(card: CardEntity, onClick: () -> Unit, onDelete: () -> Unit) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick),
        tonalElevation = 4.dp
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            // Placeholder for logo
            Box(
                modifier = Modifier.size(56.dp).clip(RoundedCornerShape(6.dp)).background(Color(android.graphics.Color.parseColor(card.colorHex))),
                contentAlignment = Alignment.Center
            ) {
                Text(card.name.take(1).uppercase(), color = Color.White)
            }

            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(card.name, style = MaterialTheme.typography.titleMedium, maxLines = 1, overflow = TextOverflow.Ellipsis)
                Text(card.company ?: "", style = MaterialTheme.typography.bodySmall)
                Text(card.phone ?: "", style = MaterialTheme.typography.bodySmall)
            }

            IconButton(onClick = onDelete) {
                Icon(painter = painterResource(android.R.drawable.ic_menu_delete), contentDescription = "Delete")
            }
        }
    }
}
