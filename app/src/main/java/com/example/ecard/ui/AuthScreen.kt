package com.example.ecard.ui

import androidx.compose.runtime.*
import androidx.compose.material3.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import com.example.ecard.theme.LightPink
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.background
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.ecard.theme.Pink
import com.example.ecard.theme.PinkDark
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.derivedStateOf

@Composable
fun AuthScreen(
    onAuthenticated: () -> Unit,
    onRegisterClick: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var showPassword by remember { mutableStateOf(false) }
    var validationError by remember { mutableStateOf<String?>(null) }

    val authState by viewModel.authState.collectAsState()
    val isLoading by remember(authState) {
        derivedStateOf { authState is AuthState.Loading }
    }

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Success -> {
                onAuthenticated()
            }
            else -> {}
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LightPink
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(
                        brush = Brush.linearGradient(listOf(Pink, PinkDark)),
                        shape = RoundedCornerShape(24.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CreditCard,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
            Text(
                "Добро пожаловать",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Войдите в свой аккаунт",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = username,
                onValueChange = {
                    username = it
                    validationError = null
                },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                isError = validationError != null
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = {
                    password = it
                    validationError = null
                },
                label = { Text("Пароль") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                trailingIcon = {
                    IconButton(onClick = { showPassword = !showPassword }) {
                        Icon(
                            if (showPassword) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Показать пароль"
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                isError = validationError != null
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
                TextButton(onClick = onForgotPassword) {
                    Text("Забыли пароль?")
                }
            }
            Spacer(Modifier.height(4.dp))

            // Отображение ошибок
            validationError?.let { error ->
                Text(
                    error,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Spacer(Modifier.height(8.dp))
            }

            GradientButton(
                text = if (isLoading) "Вход..." else "Войти",
                onClick = {
                    // Валидация перед вызовом ViewModel
                    if (username.isBlank() || password.isBlank()) {
                        validationError = "Заполните все поля"
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                        validationError = "Введите корректный email"
                    } else {
                        validationError = null
                        viewModel.login(username, password) { result ->
                            result.fold(
                                onSuccess = {
                                    // Успех обрабатывается через authState
                                },
                                onFailure = { exception ->
                                    validationError = exception.message ?: "Ошибка входа"
                                }
                            )
                        }
                    }
                },
                enabled = !isLoading
            )

            Spacer(Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Нет аккаунта? ",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
                TextButton(onClick = onRegisterClick) {
                    Text("Зарегистрироваться", color = PinkDark)
                }
            }
        }
    }
}

