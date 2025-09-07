package com.fjapps.brujosexpress.admin.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fjapps.brujosexpress.admin.data.di.rememberSettingsViewModel
import com.fjapps.brujosexpress.admin.ui.model.AdminStore
import com.fjapps.brujosexpress.admin.data.di.StoreManager
import androidx.compose.ui.platform.LocalContext

data class AdminStore(
    val id: String,
    val name: String,
    val logoUrl: String?,
    val phone: String?,
    val deliveryFeeCents: Int,
    val isOpen: Boolean
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val vm = rememberSettingsViewModel()
    val context = LocalContext.current
    var store by remember { mutableStateOf(vm.store.value) }
    store = vm.store.collectAsState().value

    Scaffold(
        topBar = { TopAppBar(title = { Text("Ajustes") }) },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        vm.updateStore(store)
                        if (store.id.isNotBlank()) {
                            StoreManager.save(context, store.id)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) { Text("Guardar cambios", color = Color.White) }
                TextButton(onClick = { /* logout */ }) {
                    Icon(Icons.Default.ExitToApp, contentDescription = null)
                    Spacer(Modifier.width(4.dp))
                    Text("Cerrar sesión")
                }
            }
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AsyncImage(
                                model = store.logoUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(64.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surface)
                            )
                            Spacer(Modifier.width(12.dp))
                            Button(onClick = { /* cambiar logo */ }) { Text("Cambiar") }
                        }
                        OutlinedTextField(
                            value = store.name,
                            onValueChange = { store = store.copy(name = it) },
                            label = { Text("Nombre*") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = store.phone.orEmpty(),
                            onValueChange = { store = store.copy(phone = it) },
                            label = { Text("Teléfono") },
                            modifier = Modifier.fillMaxWidth()
                        )
                        OutlinedTextField(
                            value = "${store.deliveryFeeCents / 100.0}",
                            onValueChange = { v ->
                                val cents = (v.toDoubleOrNull() ?: 0.0 * 100).toInt()
                                store = store.copy(deliveryFeeCents = cents)
                            },
                            label = { Text("Delivery fee (MXN)") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            item {
                Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Horario", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("Abierto")
                            Spacer(Modifier.width(8.dp))
                            Switch(
                                checked = store.isOpen,
                                onCheckedChange = { store = store.copy(isOpen = it) },
                                colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary)
                            )
                        }
                        OutlinedTextField(value = "9:00 - 21:00", onValueChange = {}, label = { Text("Horario de atención") })
                    }
                }
            }

            item {
                val context = LocalContext.current
                Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Ubicación", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        OutlinedTextField(value = "Dirección de la tienda", onValueChange = {}, label = { Text("Dirección") })
                        OutlinedButton(onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=Brujos+Express"))
                            context.startActivity(intent)
                        }) { Text("Ver en mapa") }
                    }
                }
            }

            item {
                var admins by remember { mutableStateOf(mutableListOf("admin@be.com", "ops@be.com")) }
                var showDialog by remember { mutableStateOf(false) }
                var newAdmin by remember { mutableStateOf("") }
                Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                    Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("Usuarios", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        admins.forEach { email ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(email)
                                TextButton(onClick = { admins.remove(email) }) { Text("Eliminar", color = MaterialTheme.colorScheme.error) }
                            }
                        }
                        Button(
                            onClick = { showDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                        ) { Text("Agregar admin", color = Color.White) }

                        if (showDialog) {
                            AlertDialog(
                                onDismissRequest = { showDialog = false },
                                confirmButton = {
                                    TextButton(onClick = {
                                        if (newAdmin.isNotBlank()) admins.add(newAdmin)
                                        newAdmin = ""
                                        showDialog = false
                                    }) { Text("Agregar") }
                                },
                                dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Cancelar") } },
                                title = { Text("Nuevo admin") },
                                text = {
                                    OutlinedTextField(value = newAdmin, onValueChange = { newAdmin = it }, label = { Text("Correo o teléfono") })
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}


