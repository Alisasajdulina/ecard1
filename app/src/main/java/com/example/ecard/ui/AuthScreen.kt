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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.ecard.theme.Pink
import com.example.ecard.theme.PinkDark
<<<<<<< HEAD
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AuthScreen(
    onAuthenticated: () -> Unit, 
    onRegisterClick: () -> Unit = {},
    onForgotPassword: () -> Unit = {},
    viewModel: AuthViewModel = viewModel()
) {
=======

@Composable
fun AuthScreen(onAuthenticated: () -> Unit, onRegisterClick: () -> Unit = {}) {
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var showPassword by remember { mutableStateOf(false) }
<<<<<<< HEAD
    var isLoading by remember { mutableStateOf(false) }
    
    val authState by viewModel.authState.collectAsState()
=======
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4

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
            Text("Добро пожаловать", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
            Text("Войдите в свой аккаунт", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Spacer(Modifier.height(24.dp))
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Email") },
                leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp)
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
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
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
            )
            Spacer(Modifier.height(8.dp))
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.CenterEnd
            ) {
<<<<<<< HEAD
                TextButton(onClick = { onForgotPassword() }) {
=======
                TextButton(onClick = { /* TODO: forgot pass */ }) {
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
                    Text("Забыли пароль?")
                }
            }
            Spacer(Modifier.height(4.dp))
<<<<<<< HEAD
            GradientButton(
                text = if (isLoading) "Вход..." else "Войти",
                onClick = {
                    // Валидация
                    if (username.isBlank() || password.isBlank()) {
                        error = "Заполните все поля"
                    } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(username).matches()) {
                        error = "Введите корректный email"
                    } else {
                        error = null
                        isLoading = true
                        viewModel.login(username, password) { result ->
                            isLoading = false
                            result.fold(
                                onSuccess = {
                                    onAuthenticated()
                                },
                                onFailure = { exception ->
                                    error = exception.message ?: "Ошибка входа"
                                }
                            )
                        }
                    }
                },
                enabled = !isLoading
            )
            
            LaunchedEffect(authState) {
                when (authState) {
                    is AuthState.Error -> {
                        error = authState.message
                        isLoading = false
                    }
                    is AuthState.Success -> {
                        onAuthenticated()
                    }
                    else -> {}
                }
            }
            
            error?.let { 
                Text(
                    it, 
                    color = MaterialTheme.colorScheme.error, 
                    modifier = Modifier.padding(top = 8.dp)
                ) 
            }
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
=======
            GradientButton("Войти") {
                // простая заглушка авторизации
                if (username == "user" && password == "1234") {
                    error = null
                    onAuthenticated()
                } else {
                    error = "Неверные учетные данные. Подсказка: user / 1234"
                }
            }
            error?.let { Text(it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp)) }
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
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
        }
    }
}
