package com.fjapps.brujosexpress.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.fjapps.brujosexpress.data.repository.ProductRepository
import com.fjapps.brujosexpress.navigation.Screen
import com.fjapps.brujosexpress.ui.components.ProductCard
import com.fjapps.brujosexpress.ui.theme.Purple500
import com.fjapps.brujosexpress.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RestaurantCatalogScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel()
) {
    val products = remember { ProductRepository().getRestaurantProducts() }
    val cartItemCount by cartViewModel.cartItems.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Restaurantes",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate(Screen.Cart.route) }
                    ) {
                        BadgedBox(
                            badge = {
                                Badge(
                                    containerColor = Purple500,
                                    contentColor = Color.White
                                ) { Text(text = cartItemCount.size.toString()) }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart,
                                contentDescription = "Carrito"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            item {
                Text(
                    text = "Platos Disponibles",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "Deliciosos platos de los mejores restaurantes de la ciudad",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
            
            items(products) { product ->
                ProductCard(
                    product = product,
                    onAddToCart = { cartViewModel.addToCart(product) }
                )
            }
        }
    }
}

