package com.example.ecard.ui

import android.net.Uri
import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.saveable.rememberSaveable
import com.example.ecard.data.CardEntity
import kotlinx.coroutines.launch
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarHost

@Composable
fun CardEditorScreen(
    viewModel: CardViewModel,
    cardId: Long,
    onBack: () -> Unit
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var editing by remember { mutableStateOf<CardEntity?>(null) }

    LaunchedEffect(cardId) {
        if (cardId != 0L) {
            editing = viewModel.getById(cardId)
        } else {
            editing = CardEntity(name = "", company = null, phone = null, email = null)
        }
    }

    val name = rememberSaveable { mutableStateOf(editing?.name ?: "") }
    val company = rememberSaveable { mutableStateOf(editing?.company ?: "") }
    val phone = rememberSaveable { mutableStateOf(editing?.phone ?: "") }
    val email = rememberSaveable { mutableStateOf(editing?.email ?: "") }
    val color = rememberSaveable { mutableStateOf(editing?.colorHex ?: "#FFFFFF") }
    val logoUriStr = rememberSaveable { mutableStateOf(editing?.logoUri) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (cardId == 0L) "Создать" else "Редактировать") },
                navigationIcon = { IconButton(onClick = onBack) { Text("<") } }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(value = name.value, onValueChange = { name.value = it }, label = { Text("ФИО") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = company.value, onValueChange = { company.value = it }, label = { Text("Компания") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = phone.value, onValueChange = { phone.value = it }, label = { Text("Телефон") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = email.value, onValueChange = { email.value = it }, label = { Text("Email") })
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(value = color.value, onValueChange = { color.value = it }, label = { Text("Цвет (HEX)") })
            Spacer(Modifier.height(12.dp))

            Row {
                Button(onClick = {
                    // save
                    val card = editing?.copy(
                        name = name.value.trim(),
                        company = company.value.trim().ifEmpty { null },
                        phone = phone.value.trim().ifEmpty { null },
                        email = email.value.trim().ifEmpty { null },
                        colorHex = color.value.trim().ifEmpty { "#FFFFFF" },
                        logoUri = logoUriStr.value
                    ) ?: CardEntity(name = name.value.trim(), company = company.value.trim().ifEmpty { null }, phone = phone.value.trim().ifEmpty { null }, email = email.value.trim().ifEmpty { null }, colorHex = color.value, logoUri = logoUriStr.value)

                    scope.launch {
                        viewModel.createOrUpdate(card) { result ->
                            result.fold(onSuccess = {
                                scope.launch { snackbarHostState.showSnackbar("Сохранено") }
                                onBack()
                            }, onFailure = {
                                scope.launch { snackbarHostState.showSnackbar("Ошибка: ${it.localizedMessage}") }
                            })
                        }
                    }
                }) { Text("Сохранить") }

                Spacer(Modifier.width(12.dp))

                Button(onClick = {
                    // экспорт в PNG/PDF можно вызвать здесь; оставлю хуки для ExportUtils
                }) { Text("Экспорт") }
            }
        }
    }
}
