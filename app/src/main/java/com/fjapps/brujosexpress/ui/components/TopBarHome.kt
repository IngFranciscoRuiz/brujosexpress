package com.fjapps.brujosexpress.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHome(
    cartCount: Int,
    onLocationClick: () -> Unit,
    onSearchClick: () -> Unit,
    onCartClick: () -> Unit,
    onNotificationsClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "Brujos Express",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        navigationIcon = {
            IconButton(onClick = onLocationClick) {
                Icon(Icons.Default.LocationOn, contentDescription = "Ubicación", tint = MaterialTheme.colorScheme.onSurface)
            }
        },
        actions = {
            IconButton(onClick = onSearchClick) {
                Icon(Icons.Default.Search, contentDescription = "Buscar", tint = MaterialTheme.colorScheme.onSurface)
            }
            IconButton(onClick = onCartClick) {
                BadgedBox(badge = {
                    if (cartCount > 0) Badge { Text(cartCount.toString()) }
                }) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Carrito",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            IconButton(onClick = onNotificationsClick) {
                Icon(Icons.Default.Notifications, contentDescription = "Notificaciones", tint = MaterialTheme.colorScheme.onSurface)
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}
