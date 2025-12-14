package com.example.ecard.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.Typography

private val LightColors = lightColorScheme(
    primary = Pink,
    onPrimary = OnPink,
    secondary = PinkLight,
    onSecondary = OnLightPink,
    background = LightPink,
    surface = CardSurface,
    onSurface = OnLightPink,
    outline = DividerPink,
    tertiary = PinkDark
)

private val DarkColors = darkColorScheme(
    primary = DarkPink,
    onPrimary = OnPink,
    secondary = DarkPinkDark,
    onSecondary = OnPink,
    background = DarkBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    outline = DividerPink,
    tertiary = DarkPinkDark
)

object ThemeState {
    var isDarkTheme by mutableStateOf(false)
}

@Composable
fun ECardTheme(
    darkTheme: Boolean = isSystemInDarkTheme() || ThemeState.isDarkTheme,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors
    
    MaterialTheme(
        colorScheme = colorScheme,
        typography = ECardTypography,
        content = content
    )
}
