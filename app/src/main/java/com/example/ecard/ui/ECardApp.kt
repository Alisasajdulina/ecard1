package com.example.ecard.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
<<<<<<< HEAD
                onRegisterClick = { nav.navigate("register") },
                onForgotPassword = { nav.navigate("forgot_password") }
=======
                onRegisterClick = { nav.navigate("register") }
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
            )
        }

        composable("register") {
            RegisterScreen(
                onRegistered = { nav.navigate("list") },
                onBack = { nav.popBackStack() }
<<<<<<< HEAD
            )
        }

        composable("forgot_password") {
            ForgotPasswordScreen(
                onBack = { nav.popBackStack() },
                onPasswordReset = { nav.navigate("auth") }
=======
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
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
<<<<<<< HEAD
            SettingsScreen(
                onBack = { nav.popBackStack() },
                onLogout = {
                    nav.navigate("auth") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
=======
            SettingsScreen(onBack = { nav.popBackStack() })
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
        }

        composable("templates") {
            TemplateSelectorScreen(
                selectedTemplateId = null,
                onTemplateSelected = { template ->
<<<<<<< HEAD
                    // Сохраняем выбранный шаблон в savedStateHandle текущего экрана
                    nav.currentBackStackEntry?.savedStateHandle?.set("template", template.id)
                    nav.navigate("edit/0")
=======
                    // Сохраняем выбранный шаблон и переходим к редактированию
                    nav.navigate("edit/0?template=${template.id}")
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
                },
                onBack = { nav.popBackStack() }
            )
        }

        composable(
            route = "edit/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
<<<<<<< HEAD
            // Извлекаем template из savedStateHandle текущего экрана (установлен на предыдущем экране)
            val template = remember { 
                backStackEntry.savedStateHandle.get<String>("template")
            }
            // Очищаем template после использования
            LaunchedEffect(Unit) {
                if (template != null) {
                    backStackEntry.savedStateHandle.remove<String>("template")
                }
            }
=======
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
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
