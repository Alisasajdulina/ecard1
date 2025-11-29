package com.example.ecard

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.ecard.ui.ECardApp
import com.example.ecard.theme.ECardTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ECardTheme {
                ECardApp()
            }
        }
    }
}
