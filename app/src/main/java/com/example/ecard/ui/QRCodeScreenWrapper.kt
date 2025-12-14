package com.example.ecard.ui

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch

@Composable
fun QRCodeScreenWrapper(
    viewModel: CardViewModel,
    cardId: Long,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    var card by remember { mutableStateOf<com.example.ecard.data.CardEntity?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(cardId) {
        card = viewModel.getById(cardId)
    }

    card?.let {
        QRCodeScreen(
            card = it,
            onBack = onBack
        )
    } ?: androidx.compose.material3.CircularProgressIndicator()
}

