package com.example.ecard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ecard.theme.ECardTheme
import com.example.ecard.ui.ECardApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ECardTheme(darkTheme = com.example.ecard.theme.ThemeState.isDarkTheme) {
                ECardApp()
            }
        }
    }
}
