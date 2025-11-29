package com.example.ecard.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.ecard.ui.CardListScreen
import com.example.ecard.ui.CardEditorScreen
import com.example.ecard.ui.AuthScreen

@Composable
fun ECardApp() {
    val nav = rememberNavController()
    val vm: CardViewModel = viewModel()

    NavHost(navController = nav, startDestination = "auth") {
        composable("auth") {
            AuthScreen(
                onAuthenticated = { nav.navigate("list") }
            )
        }

        composable("list") {
            CardListScreen(
                viewModel = vm,
                onCreateNew = { nav.navigate("edit/0") },
                onEdit = { id -> nav.navigate("edit/$id") }
            )
        }

        composable(
            "edit/{id}",
            arguments = listOf(navArgument("id") { defaultValue = 0L })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toLongOrNull() ?: 0L
            CardEditorScreen(
                viewModel = vm,
                cardId = id,
                onBack = { nav.popBackStack() }
            )
        }
    }
}
