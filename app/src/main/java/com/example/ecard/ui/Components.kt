package com.example.ecard.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import com.example.ecard.theme.Pink
import com.example.ecard.theme.PinkDark

@Composable
fun GradientButton(
    text: String, 
    onClick: () -> Unit,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(containerColor = androidx.compose.ui.graphics.Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(Pink, PinkDark)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(text, color = MaterialTheme.colorScheme.onPrimary)
        }
    }
}

@Composable
fun ColorChip(selected: Boolean, color: Color, onClick: () -> Unit) {
    val border = if (selected) BorderStroke(2.dp, PinkDark) else null
    Surface(
        modifier = Modifier.size(36.dp),
        color = color,
        shape = CircleShape,
        border = border,
        onClick = onClick
    ) {}
}
