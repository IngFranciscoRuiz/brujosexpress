package com.fjapps.brujosexpress.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.background
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import com.fjapps.brujosexpress.ui.models.CategoryUi

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryCarousel(
    categories: List<CategoryUi>,
    onClick: (CategoryUi) -> Unit
) {
    val state = rememberLazyListState()
    val fling = rememberSnapFlingBehavior(lazyListState = state)
    LazyRow(
        state = state,
        flingBehavior = fling,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(categories) { cat ->
            Surface(
                tonalElevation = 2.dp,
                shadowElevation = 2.dp,
                onClick = { onClick(cat) },
                shape = RoundedCornerShape(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .width(220.dp)
                        .height(130.dp)
                ) {
                    AsyncImage(
                        model = cat.imageUrl,
                        contentDescription = cat.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(16.dp))
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    0f to Color(0x00000000),
                                    1f to Color(0x336E56CF)
                                )
                            )
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(12.dp)
                    ) {
                        Text(text = cat.emoji, fontWeight = FontWeight.Bold, color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = cat.name,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}


