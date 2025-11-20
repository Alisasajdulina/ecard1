
package com.example.ecardnarwhal.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.Dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun CardListScreen(onEdit: (Int) -> Unit, onCreate: () -> Unit, vm: CardViewModel = viewModel()) {
    val cards by vm.cards.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreate,
                containerColor = Color(0xFF89C8FF)
            ) { Text("+", color = Color.White) }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(Color(0xFFF2F6FF))
        ) {
            items(cards) { card ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(24.dp),
                            ambientColor = Color(0xFFB0C4DE),
                            spotColor = Color(0xFFE0E9FF)
                        )
                        .background(Color.White, shape = RoundedCornerShape(24.dp))
                        .clickable { onEdit }
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text(card.name, style = MaterialTheme.typography.titleLarge, color = Color(0xFF3A4F7A))
                        Text(card.company, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF6C7BA1))
                    }
                }
            }

        }
    }
}
