package com.fjapps.brujosexpress.ui.components

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.fjapps.brujosexpress.ui.models.FeaturedUi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun FeaturedCarousel(
    items: List<FeaturedUi>,
    onAdd: (FeaturedUi) -> Unit
) {
    val state = rememberLazyListState()
    val fling = rememberSnapFlingBehavior(state)
    LazyRow(
        state = state,
        flingBehavior = fling,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items) { item ->
            FeaturedCard(item = item, onAdd = onAdd)
        }
    }
}

@Composable
private fun FeaturedCard(
    item: FeaturedUi,
    onAdd: (FeaturedUi) -> Unit
) {
    var added by remember { mutableStateOf(false) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(190.dp)
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            )

            if (item.discountPercent != null) {
                Surface(
                    color = Color(0xFFFF8A00),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(999.dp),
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = "-${item.discountPercent}%",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Button(
                onClick = {
                    added = true
                    onAdd(item)
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
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = item.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold, color = Color(0xFF1B1B1B))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = "⭐ ${item.rating}")
            Text(text = "🕑 ${item.etaMinutes} min")
            Text(text = "🚚 $ ${item.deliveryFee}")
        }
    }
}


