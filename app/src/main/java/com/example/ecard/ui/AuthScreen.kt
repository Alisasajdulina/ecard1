
package com.example.ecardnarwhal.ui

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys

@Composable
fun AuthScreen(onAuth: ()->Unit) {
    val ctx = LocalContext.current
    val prefs = remember { createEncryptedPrefs(ctx) }
    var pin by remember { mutableStateOf("") }
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.Center) {
        Text("Введите PIN", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(value = pin, onValueChange = { pin = it }, label = { Text("PIN") })
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            val stored = prefs.getString("pin", null)
            if (stored == null) {
                prefs.edit().putString("pin", pin).apply()
                onAuth()
            } else if (stored == pin) {
                onAuth()
            }
        }) { Text("Войти") }
    }
}

fun createEncryptedPrefs(ctx: Context): SharedPreferences {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
    return EncryptedSharedPreferences.create(
        "ecard_prefs",
        masterKeyAlias,
        ctx,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
}
