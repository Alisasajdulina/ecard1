package com.example.ecard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun ProfileScreen(nav: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("Ваш профиль", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(20.dp))

        GradientButton("Создать визитку") {
            nav.navigate("create")
        }

        Spacer(Modifier.height(10.dp))

        GradientButton("Ваши визитки") {
            nav.navigate("list")
        }
    }
}
