package com.example.ecard.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
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

    NavHost(navController = nav, startDestination = "auth") {
        composable("auth") {
            AuthScreen(
                onAuthenticated = { nav.navigate("list") },
                onRegisterClick = { nav.navigate("register") }
            )
        }

        composable("register") {
            RegisterScreen(
                onRegistered = { nav.navigate("list") },
                onBack = { nav.popBackStack() }
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
            SettingsScreen(onBack = { nav.popBackStack() })
        }

        composable("templates") {
            TemplateSelectorScreen(
                selectedTemplateId = null,
                onTemplateSelected = { template ->
                    // Сохраняем выбранный шаблон и переходим к редактированию
                    nav.navigate("edit/0?template=${template.id}")
                },
                onBack = { nav.popBackStack() }
            )
        }

        composable(
            route = "edit/{id}",
            arguments = listOf(navArgument("id") { type = NavType.LongType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getLong("id") ?: 0L
            CardEditorScreen(
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
