package com.example.ecard.ui

<<<<<<< HEAD
import androidx.compose.foundation.background
=======
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
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

<<<<<<< HEAD
@OptIn(ExperimentalMaterial3Api::class)
=======
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
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
            androidx.compose.foundation.BorderStroke(3.dp, PinkDark)
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
<<<<<<< HEAD
                    val textColor = if (template.id == "minimal_white") {
                        Color.Black
                    } else {
                        Color.White
                    }
                    Text(
                        template.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = textColor
=======
                    Text(
                        template.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
                    )
                    Text(
                        template.description,
                        style = MaterialTheme.typography.bodySmall,
<<<<<<< HEAD
                        color = textColor.copy(alpha = 0.9f)
=======
                        color = Color.White.copy(alpha = 0.9f)
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
                    )
                }
                
                if (isSelected) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
<<<<<<< HEAD
                        val iconColor = if (template.id == "minimal_white") {
                            Color.Black
                        } else {
                            Color.White
                        }
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Выбрано",
                            tint = iconColor,
=======
                        Icon(
                            Icons.Default.Check,
                            contentDescription = "Выбрано",
                            tint = Color.White,
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

