package com.example.ecard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun CreateCardScreen(nav: NavController) {
    var name by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("Создание визитки", style = MaterialTheme.typography.titleLarge)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Имя") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = company,
            onValueChange = { company = it },
            label = { Text("Компания") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(30.dp))

        GradientButton("Сохранить") {
            nav.popBackStack()
        }
    }
}
