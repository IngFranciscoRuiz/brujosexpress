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
import com.fjapps.brujosexpress.ui.models.CategoryUi
import com.fjapps.brujosexpress.ui.models.FeaturedUi

@Composable
fun HomeScreen(navController: NavController) {
    var selected by remember { mutableStateOf("Todos") }
    val categories = listOf("Todos","Restaurantes","Tienda","Bebidas","Hamburguesas","Pizza","Farmacia")
    val cartCount = 0

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
                    CategoryUi("c3","Pizzas","🍕","https://images.unsplash.com/photo-1548365328-8b8490a5b3ac?w=800"),
                    CategoryUi("c4","Tortas","🥪","https://images.unsplash.com/photo-1504754524776-8f4f37790ca0?w=800"),
                    CategoryUi("c5","Bebidas","🥤","https://images.unsplash.com/photo-1513558161293-c96bdfbd84a1?w=800"),
                    CategoryUi("c6","Abarrotes","🛒","https://images.unsplash.com/photo-1586201375761-83865001e31c?w=800")
                ),
                onClick = { clicked ->
                    selected = clicked.name
                    when (clicked.name) {
                        "Mariscos","Hamburguesas","Pizzas","Tortas" -> navController.navigate(Screen.RestaurantCatalog.route)
                        else -> navController.navigate(Screen.GroceryCatalog.route)
                    }
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
                TextButton(onClick = { navController.navigate(Screen.RestaurantCatalog.route) }) {
                    Text("Ver más")
                }
            }

            FeaturedCarousel(
                items = listOf(
                    FeaturedUi("f1","Taquería El Sol","https://images.unsplash.com/photo-1604908554055-45c7b4b6a85b?w=1200",4.6,20,26,30),
                    FeaturedUi("f2","Pizzería Don Mario","https://images.unsplash.com/photo-1548365328-8b8490a5b3ac?w=1200",4.4,25,30,null),
                    FeaturedUi("f3","Burgers House","https://images.unsplash.com/photo-1561758033-d89a9ad46330?w=1200",4.5,18,24,20)
                ),
                onAdd = { /* add to cart */ }
            )

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

