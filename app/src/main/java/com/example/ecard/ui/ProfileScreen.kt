package com.example.ecard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    onCreateCard: ()->Unit = {},
    onMyCards: ()->Unit = {},
    onTemplates: ()->Unit = {},
    onSettings: ()->Unit = {},
    onLogout: ()->Unit = {}
) {
    ECardTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(28.dp))
                ProfileHeader(avatarPainter = null, name = "Анна Иванова", email = "anna@example.com", online = true)
                Spacer(Modifier.height(6.dp))
                // small pill showing card count
                Card(shape = RoundedCornerShape(16.dp), modifier = Modifier.padding(vertical = 6.dp)) {
                    Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Bookmark, contentDescription = null, tint = AccentPink)
                        Spacer(Modifier.width(8.dp))
                        Text("3 визиток", color = AccentPink)
                    }
                }
                Spacer(Modifier.height(12.dp))
                // info cards
                InfoCard(
                    icon = { Icon(Icons.Default.Email, contentDescription = null, tint = AccentPink) },
                    title = "Email",
                    subtitle = "anna@example.com",
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                InfoCard(
                    icon = { Icon(Icons.Default.Phone, contentDescription = null, tint = AccentPink) },
                    title = "Телефон",
                    subtitle = "+7 (999) 123-45-67",
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                InfoCard(
                    icon = { Icon(Icons.Default.Work, contentDescription = null, tint = AccentPink) },
                    title = "Компания",
                    subtitle = "Design Studio",
                    modifier = Modifier.padding(vertical = 6.dp)
                )
                Spacer(Modifier.height(12.dp))
                // Primary action
                GradientButton(
                    text = "Создать визитку",
                    modifier = Modifier.padding(vertical = 10.dp),
                    onClick = onCreateCard
                )
                Spacer(Modifier.height(8.dp))
                MenuItemRow(icon = { Icon(Icons.Default.BookmarkBorder, contentDescription = null, tint = AccentPink) }, text = "Мои визитки (3)", onClick = onMyCards)
                Spacer(Modifier.height(8.dp))
                MenuItemRow(icon = { Icon(Icons.Default.GridView, contentDescription = null, tint = AccentPink) }, text = "Шаблоны визиток", onClick = onTemplates)
                Spacer(Modifier.height(8.dp))
                MenuItemRow(icon = { Icon(Icons.Default.Settings, contentDescription = null, tint = AccentPink) }, text = "Настройки", onClick = onSettings)
                Spacer(Modifier.height(8.dp))
                MenuItemRow(icon = { Icon(Icons.Default.ExitToApp, contentDescription = null, tint = AccentPink) }, text = "Выйти", onClick = onLogout)
                Spacer(Modifier.height(24.dp))
            }
        }
    }
}
