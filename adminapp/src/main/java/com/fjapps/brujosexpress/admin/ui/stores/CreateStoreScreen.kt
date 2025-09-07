package com.fjapps.brujosexpress.admin.ui.stores

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import androidx.compose.ui.platform.LocalContext
import com.fjapps.brujosexpress.admin.data.di.StoreManager
import com.fjapps.brujosexpress.admin.navigation.AdminRoute

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateStoreScreen(navController: NavController) {
    var email by remember { mutableStateOf("") }
    var storeId by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var fee by remember { mutableStateOf("") }
    var isOpen by remember { mutableStateOf(true) }
    var loading by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    Scaffold(topBar = { TopAppBar(title = { Text("Crear tienda") }) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email del dueño*") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = storeId, onValueChange = { storeId = it.trim() }, label = { Text("Store ID*") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Nombre de tienda*") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = phone, onValueChange = { phone = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
            OutlinedTextField(value = fee, onValueChange = { fee = it.filter { ch -> ch.isDigit() } }, label = { Text("Delivery fee (centavos)") }, modifier = Modifier.fillMaxWidth())
            Row { Text("Abierta") ; Spacer(Modifier.width(8.dp)); Switch(checked = isOpen, onCheckedChange = { isOpen = it }) }

            message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }

            Button(
                onClick = {
                    if (email.isBlank() || storeId.isBlank() || name.isBlank()) {
                        message = "Completa los campos marcados con *"
                        return@Button
                    }
                    loading = true
                    val callable = Firebase.functions.getHttpsCallable("createStoreOwner")
                    callable.call(
                        mapOf(
                            "email" to email,
                            "storeId" to storeId,
                            "store" to mapOf(
                                "name" to name,
                                "phone" to phone,
                                "deliveryFeeCents" to (fee.toIntOrNull() ?: 0),
                                "isOpen" to isOpen
                            )
                        )
                    ).addOnCompleteListener { task ->
                        loading = false
                        if (task.isSuccessful) {
                            // Persistir storeId activo y navegar a Productos
                            StoreManager.save(context, storeId)
                            val link = (task.result?.data as? Map<*, *>)?.get("resetLink") as? String
                            message = "Tienda creada. ${link?.let { "Se envió enlace de contraseña." } ?: ""}"
                            navController.navigate(AdminRoute.Products.route) {
                                popUpTo(AdminRoute.Dashboard.route)
                                launchSingleTop = true
                            }
                        } else {
                            message = task.exception?.message ?: "Error al crear tienda"
                        }
                    }
                },
                enabled = !loading,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (loading) { Text("Creando...", color = Color.White); Spacer(Modifier.width(8.dp)) }
                Text("Crear tienda", color = Color.White)
            }
        }
    }
}


