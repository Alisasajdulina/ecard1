package com.example.ecard.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Business
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreateCardScreen(
    onSave: (CardData)->Unit = {},
    onBack: ()->Unit = {}
) {
    var name by remember { mutableStateOf("") }
    var position by remember { mutableStateOf("") }
    var company by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    ECardTheme {
        Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)) {

                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "back")
                }

                Spacer(Modifier.height(10.dp))
                Text("Создать визитку", style = MaterialTheme.typography.titleMedium)

                Spacer(Modifier.height(16.dp))

                // inputs
                RoundedTextField(label = "Имя", value = name, onValueChange = { name = it }, leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) }, placeholder = "Анна Иванова", modifier = Modifier.padding(vertical = 6.dp))
                RoundedTextField(label = "Должность", value = position, onValueChange = { position = it }, leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) }, placeholder = "Дизайнер", modifier = Modifier.padding(vertical = 6.dp))
                RoundedTextField(label = "Компания", value = company, onValueChange = { company = it }, leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) }, placeholder = "Design Studio", modifier = Modifier.padding(vertical = 6.dp))
                RoundedTextField(label = "Email", value = email, onValueChange = { email = it }, leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) }, placeholder = "anna@example.com", modifier = Modifier.padding(vertical = 6.dp))
                RoundedTextField(label = "Телефон", value = phone, onValueChange = { phone = it }, leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) }, placeholder = "+7 (999) 123-45-67", modifier = Modifier.padding(vertical = 6.dp))

                Spacer(Modifier.height(20.dp))

                // preview card (simple)
                Card(shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(8.dp), modifier = Modifier.fillMaxWidth().height(140.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(if (name.isNotBlank()) name else "Анна Иванова", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                        Text(if (position.isNotBlank()) position else "Дизайнер", color = AccentPink)
                        Spacer(Modifier.height(8.dp))
                        Text(if (company.isNotBlank()) company else "Design Studio")
                        Spacer(Modifier.height(8.dp))
                        Row {
                            Text(email.ifBlank { "anna@example.com" })
                            Spacer(Modifier.width(12.dp))
                            Text(phone.ifBlank { "+7 (999) 123-45-67" })
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))
                GradientButton(text = "Сохранить визитку", onClick = {
                    val card = CardData(name, position, company, email, phone)
                    onSave(card)
                })
            }
        }
    }
}

data class CardData(
    val name: String,
    val position: String,
    val company: String,
    val email: String,
    val phone: String
)
