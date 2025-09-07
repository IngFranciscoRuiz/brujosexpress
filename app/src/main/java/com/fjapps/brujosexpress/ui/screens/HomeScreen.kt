package com.fjapps.brujosexpress.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fjapps.brujosexpress.navigation.Screen
import com.fjapps.brujosexpress.navigation.BottomNavBar
import com.fjapps.brujosexpress.ui.theme.Purple500
import com.fjapps.brujosexpress.ui.components.TopBarHome
import com.fjapps.brujosexpress.ui.components.CategoryChipsRow
import com.fjapps.brujosexpress.ui.components.RestaurantCard
import com.fjapps.brujosexpress.ui.components.StickyMiniCart
import com.fjapps.brujosexpress.ui.components.CategoryCarousel
import com.fjapps.brujosexpress.ui.components.FeaturedCarousel
import com.fjapps.brujosexpress.data.repository.FirestoreClientRepository
import com.fjapps.brujosexpress.ui.models.CategoryUi
import com.fjapps.brujosexpress.ui.models.FeaturedUi

@Composable
fun HomeScreen(navController: NavController) {
    var selected by remember { mutableStateOf("Todos") }
    val categories = listOf("Todos","Restaurantes","Tienda","Bebidas","Hamburguesas","Pizza","Mariscos","Abarrotes","Otros")
    val cartCount = 0
    var featured by remember { mutableStateOf(listOf<com.fjapps.brujosexpress.ui.models.FeaturedUi>()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            loading = true
            featured = FirestoreClientRepository().getFeatured()
            error = null
        } catch (e: Exception) {
            featured = emptyList()
            error = e.message
        } finally {
            loading = false
        }
    }

    Scaffold(
        topBar = {
            TopBarHome(
                cartCount = cartCount,
                onLocationClick = { /* TODO selector de colonia */ },
                onSearchClick = { navController.navigate(Screen.Search.route) },
                onCartClick = { navController.navigate(Screen.Cart.route) },
                onNotificationsClick = { /* TODO */ }
            )
        },
        bottomBar = { BottomNavBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Descubre",
                style = MaterialTheme.typography.headlineLarge,
                color = Color(0xFF1B1B1B)
            )
            Spacer(modifier = Modifier.height(12.dp))
            // Carrusel categorías con snap y peek
            CategoryCarousel(
                categories = listOf(
                    CategoryUi("c1","Mariscos","🦐","https://images.unsplash.com/photo-1515003197210-e0cd71810b5f?w=800"),
                    CategoryUi("c2","Hamburguesas","🍔","https://images.unsplash.com/photo-1561758033-d89a9ad46330?w=800"),
                    CategoryUi("c3","Pizza","🍕","https://images.unsplash.com/photo-1548365328-8b8490a5b3ac?w=800"),
                    CategoryUi("c4","Bebidas","🥤","https://images.unsplash.com/photo-1513558161293-c96bdfbd84a1?w=800"),
                    CategoryUi("c5","Abarrotes","🛒","https://images.unsplash.com/photo-1586201375761-83865001e31c?w=800"),
                    CategoryUi("c6","Otros","✨","https://images.unsplash.com/photo-1478147427282-58a87a120781?w=800")
                ),
                onClick = { clicked ->
                    selected = clicked.name
                    navController.navigate("category/${clicked.name}")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Destacados en Brujos Express",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFF1B1B1B)
                )
                TextButton(onClick = { navController.navigate(Screen.Stores.route) }) {
                    Text("Ver tiendas")
                }
            }

            when {
                loading -> {
                    Box(Modifier.fillMaxWidth().height(140.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
                }
                error != null -> {
                    Text("Error cargando destacados: ${'$'}error", color = Color.Red)
                }
                else -> {
                    FeaturedCarousel(
                        items = featured,
                        onAdd = { f -> navController.navigate("storeCatalog/${f.storeId}") }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            StickyMiniCart(
                visible = false,
                itemCount = 0,
                totalText = "$0.00",
                onClick = { navController.navigate(Screen.Cart.route) }
            )
        }
    }
}

