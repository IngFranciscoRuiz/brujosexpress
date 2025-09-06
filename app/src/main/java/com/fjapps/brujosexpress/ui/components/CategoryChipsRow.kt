package com.fjapps.brujosexpress.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun CategoryChipsRow(
    categories: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            val isSelected = category == selected
            Surface(
                onClick = { onSelect(category) },
                shape = RoundedCornerShape(20.dp),
                color = if (isSelected) Color(0xFFE8E3FF) else Color(0xFFF0F0F3),
                contentColor = if (isSelected) Color(0xFF6E56CF) else Color(0xFF1B1B1B)
            ) {
                Text(
                    text = category,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                )
            }
        }
    }
}
