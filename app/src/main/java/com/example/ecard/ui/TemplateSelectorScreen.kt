package com.example.ecard.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import com.example.ecard.theme.PinkDark
import androidx.compose.foundation.BorderStroke

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TemplateSelectorScreen(
    selectedTemplateId: String?,
    onTemplateSelected: (CardTemplate) -> Unit,
    onBack: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Выберите шаблон") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                }
            )
        }
    ) { padding ->
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(padding),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(CardTemplates.templates) { template ->
                TemplateCard(
                    template = template,
                    isSelected = template.id == selectedTemplateId,
                    onClick = { onTemplateSelected(template) }
                )
            }
        }
    }
}

@Composable
fun TemplateCard(
    template: CardTemplate,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val gradientColors = template.gradientColors.map {
        Color(android.graphics.Color.parseColor(it))
    }

    val textColor = if (template.id == "minimal_white") {
        Color.Black
    } else {
        Color.White
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 8.dp else 4.dp
        ),
        border = if (isSelected) {
            BorderStroke(3.dp, PinkDark)
        } else null
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.linearGradient(gradientColors)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        template.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = textColor
                    )
                    Text(
                        template.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = textColor.copy(alpha = 0.9f)
                    )
                }

                if (isSelected) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Выбрано",
                            tint = textColor,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

// Пример определения CardTemplate и CardTemplates
data class CardTemplate(
    val id: String,
    val name: String,
    val description: String,
    val gradientColors: List<String>
)

object CardTemplates {
    val templates = listOf(
        CardTemplate(
            id = "minimal_white",
            name = "Минимализм",
            description = "Белый фон, черный текст",
            gradientColors = listOf("#FFFFFF", "#F5F5F5")
        ),
        CardTemplate(
            id = "pink_gradient",
            name = "Розовый градиент",
            description = "Нежный розовый градиент",
            gradientColors = listOf("#FFE8F2", "#FFA3C8")
        )
        // Добавьте другие шаблоны по мере необходимости
    )
}