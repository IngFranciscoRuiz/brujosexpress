package com.fjapps.brujosexpress.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun RestaurantCard(
    imageUrl: String,
    name: String,
    deliveryFee: String,
    etaMinutes: String,
    rating: String,
    badgeText: String? = null,
    onAdd: () -> Unit
) {
    var added by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box {
            AsyncImage(
                model = imageUrl,
                contentDescription = name,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                contentScale = ContentScale.Crop
            )

            if (badgeText != null) {
                Surface(
                    color = Color(0xFFFF8A00),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(999.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = badgeText,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // CTA Add floating
            Button(
                onClick = {
                    added = true
                    onAdd()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF8A00), contentColor = Color.White),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
            ) {
                Text(text = if (added) "✔ Añadido" else "+ Añadir", fontWeight = FontWeight.Bold)
            }
        }
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = Color(0xFF1B1B1B))
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "$deliveryFee • $etaMinutes • ⭐ $rating", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF555555))
        }
    }
}
