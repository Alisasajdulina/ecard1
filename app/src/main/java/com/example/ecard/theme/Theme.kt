package com.example.ecard.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.TileMode

// Colors based on the mock
val LightBackground = Color(0xFFFFF1F3)
val PinkStart = Color(0xFFFF4D94)
val PinkEnd = Color(0xFFE0006E)
val AccentPink = Color(0xFFFF2D7A)
val CardBg = Color(0xFFFFFFFF)
val MutedText = Color(0xFF9A4A6A)
val SoftPink = Color(0xFFFFEEF1)

val GradientBrush = Brush.horizontalGradient(
    colors = listOf(PinkStart, PinkEnd),
    tileMode = TileMode.Clamp
)

private val LightColors = lightColorScheme(
    primary = PinkStart,
    onPrimary = Color.White,
    secondary = AccentPink,
    onSecondary = Color.White,
    background = LightBackground,
    surface = CardBg,
    onSurface = Color(0xFF3B1E33),
    error = Color(0xFFB00020)
)

@Composable
fun ECardTheme(
    useDark: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!useDark) LightColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = Typography(
            displayLarge = androidx.compose.ui.text.TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold),
            bodyMedium = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
            bodySmall = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
            titleMedium = androidx.compose.ui.text.TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Medium)
        ),
        content = content
    )
}
