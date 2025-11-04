package com.example.ecard.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import com.example.ecardnarwhal.ui.AuthScreen
import com.example.ecardnarwhal.ui.CardEditorScreen
import com.example.ecardnarwhal.ui.CardListScreen

@Composable
fun ECardApp() {
    val nav = rememberNavController()
    NavHost(navController = nav, startDestination = "auth") {
        composable("auth") { AuthScreen(onAuth = { nav.navigate("list") }) }
        composable("list") {
            CardListScreen(
                onCreate = { nav.navigate("edit/-1") },
                onEdit = { id -> nav.navigate("edit/$id") })
        }
        composable("edit/{id}") { back ->
            val id = back.arguments?.getString("id")?.toLong() ?: -1L
            CardEditorScreen(cardId = id, onSaved = { nav.popBackStack() })
        }
    }
}
