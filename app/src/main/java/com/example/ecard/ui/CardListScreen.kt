
package com.example.ecardnarwhal.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CardListScreen(onCreate: ()->Unit, onEdit: (Long)->Unit, vm: CardViewModel = viewModel()) {
    val cards by vm.cards.collectAsState()
    Scaffold(floatingActionButton = { FloatingActionButton(onClick = onCreate) { Text("+") } }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(cards) { card ->
                Card(modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onEdit(card.id) }.animateContentSize()) {
                    Column(Modifier.padding(12.dp)) {
                        Text(card.name, style = MaterialTheme.typography.titleMedium)
                        Text(card.company, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
