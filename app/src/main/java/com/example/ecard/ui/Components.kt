package com.example.ecard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.BorderStroke
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale

@Composable
fun GradientButton(
    text: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(8.dp, RoundedCornerShape(16.dp)),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        contentPadding = PaddingValues()
    ) {
        Box(
            modifier = Modifier
                .background(
                    brush = GradientBrush,
                    shape = RoundedCornerShape(16.dp)
                )
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        }
    }
}

@Composable
fun RoundedTextField(
    label: String,
    value: String,
    onValueChange: (String)->Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    placeholder: String = "",
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp),
        label = { Text(label, color = AccentPink)},
        placeholder = { Text(placeholder) },
        leadingIcon = leadingIcon,
        shape = RoundedCornerShape(12.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = SoftPink,
            unfocusedBorderColor = SoftPink,
            containerColor = CardBg,
            focusedLabelColor = AccentPink
        )
    )
}

@Composable
fun InfoCard(
    icon: @Composable (() -> Unit),
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(SoftPink),
                contentAlignment = Alignment.Center
            ) {
                icon()
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, color = AccentPink, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(4.dp))
                Text(subtitle, color = Color(0xFF3B1E33))
            }
        }
    }
}

@Composable
fun ProfileHeader(
    avatarPainter: Painter?,
    name: String,
    email: String,
    online: Boolean = true
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // avatar with circle and status
        Box {
            if (avatarPainter != null) {
                Image(
                    painter = avatarPainter,
                    contentDescription = "avatar",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                        .background(GradientBrush),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape)
                    .background(GradientBrush),
                    contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(44.dp))
                }
            }
            if (online) {
                Box(modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .offset(x = 6.dp, y = 6.dp)
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF18C944))
                    .border(BorderStroke(2.dp, Color.White), shape = CircleShape)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(name, color = Color(0xFF6E2742), fontWeight = FontWeight.Medium, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(6.dp))
        Text(email, color = AccentPink)
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun MenuItemRow(
    icon: @Composable ()->Unit,
    text: String,
    onClick: ()->Unit,
    highlighted: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (highlighted) 8.dp else 4.dp)
    ) {
        Row(Modifier.padding(horizontal = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(28.dp), contentAlignment = Alignment.Center) { icon() }
            Spacer(Modifier.width(12.dp))
            Text(text, color = Color(0xFF6E2742))
        }
    }
}
