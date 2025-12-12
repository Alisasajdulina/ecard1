package com.example.ecard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import com.example.ecard.theme.ThemeState

@Composable
fun SettingsScreen(onBack: () -> Unit) {
    val isDarkTheme = ThemeState.isDarkTheme
    
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Настройки") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Темная тема", style = MaterialTheme.typography.titleMedium)
                        Text(
                            if (isDarkTheme) "Включена" else "Выключена",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                    IconButton(onClick = {
                        ThemeState.isDarkTheme = !ThemeState.isDarkTheme
                    }) {
                        Icon(
                            if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Переключить тему"
                        )
                    }
                }
            }
        }
    }
}

