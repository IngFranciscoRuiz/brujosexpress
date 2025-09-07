package com.fjapps.brujosexpress.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fjapps.brujosexpress.data.models.Store
import com.fjapps.brujosexpress.data.repository.FirestoreClientRepository
import com.fjapps.brujosexpress.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StoreListScreen(navController: NavController) {
    var loading by remember { mutableStateOf(true) }
    var stores by remember { mutableStateOf<List<Store>>(emptyList()) }

    LaunchedEffect(Unit) {
        loading = true
        stores = try { FirestoreClientRepository().getOpenStores() } catch (_: Exception) { emptyList() }
        loading = false
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Tiendas") }) }) { padding ->
        if (loading) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Cargando...")
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(stores) { store ->
                    StoreCard(store = store, onClick = {
                        navController.navigate("storeCatalog/${store.id}")
                    })
                }
            }
        }
    }
}

@Composable
private fun StoreCard(store: Store, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = store.logoUrl,
                contentDescription = store.name,
                modifier = Modifier.size(64.dp).clip(RoundedCornerShape(12.dp))
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(store.name, style = MaterialTheme.typography.titleMedium)
                Text(if (store.isOpen) "Abierto" else "Cerrado", color = if (store.isOpen) Color(0xFF28B463) else Color.Red)
            }
            Text("${store.deliveryFeeCents / 100.0} envío")
        }
    }
}


