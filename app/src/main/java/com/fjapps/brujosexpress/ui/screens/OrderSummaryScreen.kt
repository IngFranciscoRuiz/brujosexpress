package com.fjapps.brujosexpress.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.draw.clip
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.fjapps.brujosexpress.data.models.CartItem
import com.fjapps.brujosexpress.navigation.Screen
import com.fjapps.brujosexpress.ui.theme.Purple500
import com.fjapps.brujosexpress.viewmodel.CartViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderSummaryScreen(
    navController: NavController,
    cartViewModel: CartViewModel = viewModel()
) {
    val cartItems by cartViewModel.cartItems.collectAsState()
    val total by cartViewModel.total.collectAsState()
    
    var deliveryAddress by remember { mutableStateOf("") }
    var customerName by remember { mutableStateOf("") }
    var customerPhone by remember { mutableStateOf("") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Resumen del Pedido",
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
                    text = "Información de Entrega",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Campo de nombre
                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Nombre completo") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Purple500,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Campo de teléfono
                OutlinedTextField(
                    value = customerPhone,
                    onValueChange = { customerPhone = it },
                    label = { Text("Teléfono") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Purple500,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                
                Spacer(modifier = Modifier.height(12.dp))
                
                // Campo de dirección
                OutlinedTextField(
                    value = deliveryAddress,
                    onValueChange = { deliveryAddress = it },
                    label = { Text("Dirección de entrega") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    minLines = 2,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Purple500,
                        unfocusedBorderColor = Color.Gray
                    )
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Text(
                    text = "Productos del Pedido",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
            
            // Lista de productos
            items(cartItems) { cartItem ->
                OrderItemCard(cartItem = cartItem)
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp))
                
                // Resumen del total
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Subtotal:",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "$${String.format("%.2f", total)}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Costo de envío:",
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                            Text(
                                text = "$2.99",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        
                        Divider(
                            modifier = Modifier.padding(vertical = 12.dp),
                            color = Color.LightGray
                        )
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Total:",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                            Text(
                                text = "$${String.format("%.2f", total + 2.99)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = Purple500
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        Button(
                            onClick = { 
                                // Aquí se procesaría el pago
                                navController.navigate(Screen.Tracking.route) {
                                    popUpTo(Screen.Home.route) { inclusive = true }
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Purple500),
                            shape = RoundedCornerShape(20.dp),
                            enabled = customerName.isNotBlank() && customerPhone.isNotBlank() && deliveryAddress.isNotBlank()
                        ) {
                            Text(
                                text = "Confirmar Pedido",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderItemCard(cartItem: CartItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(cartItem.product.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = cartItem.product.name,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = cartItem.product.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = "Cantidad: ${cartItem.quantity}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            
            Text(
                text = "$${String.format("%.2f", cartItem.product.price * cartItem.quantity)}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Purple500
            )
        }
    }
}

