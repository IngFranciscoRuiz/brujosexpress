package com.fjapps.brujosexpress.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavBackStackEntry
import com.fjapps.brujosexpress.data.models.Product
import com.fjapps.brujosexpress.data.repository.FirestoreClientRepository
import com.fjapps.brujosexpress.ui.components.ProductCard
import androidx.lifecycle.viewmodel.compose.viewModel
import com.fjapps.brujosexpress.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreCatalogScreen(navController: NavController, backStackEntry: NavBackStackEntry, cartViewModel: CartViewModel = viewModel()) {
    val storeId = backStackEntry.arguments?.getString("storeId") ?: return
    var loading by remember { mutableStateOf(true) }
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }

    LaunchedEffect(storeId) {
        loading = true
        products = try { FirestoreClientRepository().getProductsForStore(storeId) } catch (_: Exception) { emptyList() }
        loading = false
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Catálogo") }) }) { padding ->
        if (loading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("Cargando...") }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(products) { p ->
                    ProductCard(product = p, onAddToCart = { cartViewModel.addToCart(p) })
                }
            }
        }
    }
}


