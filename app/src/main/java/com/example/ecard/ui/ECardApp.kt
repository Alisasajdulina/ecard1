package com.example.ecard.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.*
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController

@Composable
fun ECardApp() {
    val nav = rememberNavController()
    val vm: CardViewModel = viewModel()
    val authVm: AuthViewModel = viewModel()

    // Проверяем, авторизован ли пользователь при запуске
    val startDestination = if (authVm.isLoggedIn()) "list" else "auth"

    LaunchedEffect(Unit) {
        if (authVm.isLoggedIn()) {
            nav.navigate("list") {
                popUpTo("auth") { inclusive = true }
            }
        }
    }

    NavHost(navController = nav, startDestination = startDestination) {
        composable("auth") {
            AuthScreen(
                onAuthenticated = { nav.navigate("list") },
                onRegisterClick = { nav.navigate("register") },
                onForgotPassword = { nav.navigate("forgot_password") }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegistered = { nav.navigate("list") },
                onBack = { nav.popBackStack() }
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(
                onBack = { nav.popBackStack() },
                onPasswordReset = { nav.navigate("auth") }
            )
        }

        composable("list") {
            CardListScreen(
                viewModel = vm,
                onCreateNew = { nav.navigate("templates") },
                onEdit = { id -> nav.navigate("edit/$id") },
                onShowQR = { id -> nav.navigate("qr/$id") },
                onSettings = { nav.navigate("settings") }
            )
        }

        composable("settings") {
            SettingsScreen(
                onBack = { nav.popBackStack() },
                onLogout = {
                    nav.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("templates") {
            TemplateSelectorScreen(
                selectedTemplateId = null,
                onTemplateSelected = { template ->
                    nav.currentBackStackEntry?.savedStateHandle?.set("template", template.id)
                    nav.navigate("edit/0")
                },
                onBack = { nav.popBackStack() }
            )
        }

        composable(
            route = "edit/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L

            // Извлекаем template из savedStateHandle текущего экрана
            val template = remember {
                backStackEntry.savedStateHandle.get<String>("template")
            }
            // Очищаем template после использования
            LaunchedEffect(Unit) {
                if (template != null) {
                    backStackEntry.savedStateHandle.remove<String>("template")
                }
            }

            CardEditorScreen(
                viewModel = vm,
                cardId = id,
                initialTemplateId = template?.takeIf { it.isNotBlank() },
                onBack = { nav.popBackStack() }
            )
        }

        composable(
            route = "qr/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            QRCodeScreenWrapper(
                viewModel = vm,
                cardId = id,
                onBack = { nav.popBackStack() }
            )
        }
    }
}