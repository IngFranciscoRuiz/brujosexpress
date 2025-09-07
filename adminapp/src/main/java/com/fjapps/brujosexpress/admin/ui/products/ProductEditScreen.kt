package com.fjapps.brujosexpress.admin.ui.products

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.fjapps.brujosexpress.admin.data.di.rememberProductsViewModel
import com.fjapps.brujosexpress.admin.ui.model.AdminProduct
import com.fjapps.brujosexpress.admin.data.di.StoreManager
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditScreen(navController: NavController, productId: String?) {
    val isEditing = productId != null
    val vm = rememberProductsViewModel()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val products = vm.products.collectAsState().value

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Comida") }
    var description by remember { mutableStateOf("") }
    var available by remember { mutableStateOf(true) }
    var featured by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var showSnack by remember { mutableStateOf(false) }
    val snackHostState = remember { SnackbarHostState() }
    var loading by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var existingImageUrl by remember { mutableStateOf<String?>(null) }

    // Prefill when editing
    var prefilled by remember { mutableStateOf(false) }
    LaunchedEffect(products, productId) {
        if (isEditing && !prefilled) {
            val p = products.firstOrNull { it.id == productId }
            if (p != null) {
                name = p.name
                price = (p.priceCents / 100.0).toString()
                category = p.category
                available = p.available
                featured = p.featured
                existingImageUrl = p.imageUrl
                prefilled = true
            }
        }
    }

    val pickImage = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) imageUri = uri
    }

    LaunchedEffect(showSnack) {
        if (showSnack) {
            snackHostState.showSnackbar("Producto guardado")
            showSnack = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(if (isEditing) "Editar producto" else "Nuevo producto") })
        },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = {
                        val priceValue = price.toDoubleOrNull() ?: 0.0
                        if (name.isBlank() || priceValue <= 0.0) return@Button
                        val storeId = StoreManager.currentStoreId
                        if (storeId.isNullOrBlank()) {
                            scope.launch { snackHostState.showSnackbar("Selecciona una tienda primero") }
                            return@Button
                        }
                        loading = true
                        scope.launch {
                            try {
                                val finalId = productId ?: UUID.randomUUID().toString()
                                var imageUrl: String? = existingImageUrl
                                val picked = imageUri
                                if (picked != null) {
                                    val ref = Firebase.storage.reference.child("stores/$storeId/products/$finalId.jpg")
                                    ref.putFile(picked).await()
                                    val newUrl = ref.downloadUrl.await().toString()
                                    // Delete previous image if exists and changed
                                    if (!existingImageUrl.isNullOrBlank() && existingImageUrl != newUrl) {
                                        try {
                                            Firebase.storage.getReferenceFromUrl(existingImageUrl!!).delete().await()
                                        } catch (_: Exception) { /* ignore */ }
                                    }
                                    imageUrl = newUrl
                                }
                                val product = AdminProduct(
                                    id = finalId,
                                    name = name,
                                    priceCents = (priceValue * 100).toInt(),
                                    imageUrl = imageUrl,
                                    category = category,
                                    available = available,
                                    featured = featured
                                )
                                vm.upsert(product)
                                loading = false
                                snackHostState.showSnackbar("Producto guardado")
                                navController.popBackStack()
                            } catch (e: Exception) {
                                loading = false
                                snackHostState.showSnackbar(e.message ?: "Error al guardar")
                            }
                        }
                    },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                ) { Text(if (loading) "Guardando..." else "Guardar", color = Color.White) }
                TextButton(onClick = { if (!loading) navController.popBackStack() }) { Text("Cancelar") }
                if (isEditing) {
                    TextButton(onClick = { showDeleteDialog = true }) {
                        Icon(Icons.Default.Delete, contentDescription = null, tint = MaterialTheme.colorScheme.error)
                        Spacer(Modifier.width(4.dp))
                        Text("Eliminar", color = MaterialTheme.colorScheme.error)
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(snackHostState) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
                .imePadding(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Imagen
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(140.dp)
                            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(12.dp))
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedButton(onClick = { pickImage.launch("image/*") }) { Text("Subir foto") }
                }
            }

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Nombre*") },
                isError = name.isBlank(),
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
            OutlinedTextField(
                value = price,
                onValueChange = { price = it.filter { ch -> ch.isDigit() || ch == '.' } },
                label = { Text("Precio (MXN)*") },
                isError = price.toDoubleOrNull()?.let { it <= 0.0 } ?: true,
                supportingText = {
                    if (price.toDoubleOrNull()?.let { it <= 0.0 } != false) Text("Ingrese un precio válido")
                },
                modifier = Modifier.fillMaxWidth()
            )

            // Categoría
            var expanded by remember { mutableStateOf(false) }
            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                OutlinedTextField(
                    value = category,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Categoría*") },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                )
                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    listOf("Comida", "Bebidas", "Abarrotes", "Mariscos", "Hamburguesas", "Pizza", "Otros").forEach { option ->
                        DropdownMenuItem(text = { Text(option) }, onClick = {
                            category = option
                            expanded = false
                        })
                    }
                }
            }

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                supportingText = { Text("Opcional • Máx. 200 caracteres") }
            )

            Text("Visibilidad y promoción", style = MaterialTheme.typography.titleMedium)
            Card(shape = RoundedCornerShape(12.dp), elevation = CardDefaults.cardElevation(1.dp)) {
                Column(Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Column { Text("Disponible"); Text("Aparece en el catálogo", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) }
                        Switch(
                            checked = available,
                            onCheckedChange = { available = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        Column { Text("Destacado"); Text("Muestra en carrusel de Home", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)) }
                        Switch(
                            checked = featured,
                            onCheckedChange = { featured = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteDialog = false
                    val storeId = StoreManager.currentStoreId
                    scope.launch {
                        try {
                            if (!existingImageUrl.isNullOrBlank()) {
                                try { Firebase.storage.getReferenceFromUrl(existingImageUrl!!).delete().await() } catch (_: Exception) {}
                            } else if (!storeId.isNullOrBlank() && productId != null) {
                                try { Firebase.storage.reference.child("stores/$storeId/products/$productId.jpg").delete().await() } catch (_: Exception) {}
                            }
                            if (productId != null) vm.delete(productId)
                            navController.popBackStack()
                        } catch (e: Exception) {
                            snackHostState.showSnackbar(e.message ?: "Error al eliminar")
                        }
                    }
                }) { Text("Eliminar definitivamente") }
            },
            dismissButton = { TextButton(onClick = { showDeleteDialog = false }) { Text("Cancelar") } },
            title = { Text("Eliminar producto") },
            text = { Text("Esta acción no se puede deshacer. ¿Deseas continuar?") }
        )
    }
}


