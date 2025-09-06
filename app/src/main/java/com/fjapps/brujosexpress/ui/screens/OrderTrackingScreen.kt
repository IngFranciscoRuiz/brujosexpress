package com.fjapps.brujosexpress.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.fjapps.brujosexpress.data.models.OrderStatus
import com.fjapps.brujosexpress.navigation.Screen
import com.fjapps.brujosexpress.ui.theme.Purple500
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderTrackingScreen(navController: NavController) {
    var currentStatus by remember { mutableStateOf(OrderStatus.RECEIVED) }
    
    // Simular progreso del pedido
    LaunchedEffect(key1 = true) {
        delay(3000)
        currentStatus = OrderStatus.PREPARING
        delay(4000)
        currentStatus = OrderStatus.ON_THE_WAY
        delay(5000)
        currentStatus = OrderStatus.DELIVERED
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Seguimiento del Pedido",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Título principal
            Text(
                text = "Tu pedido está en camino",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            Text(
                text = "Te mantendremos informado en cada paso",
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 40.dp)
            )
            
            // Stepper de estados
            OrderStatusStepper(currentStatus = currentStatus)
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Información adicional
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = "Información del Pedido",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Número de pedido:",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "#BE-${System.currentTimeMillis().toString().takeLast(6)}",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Tiempo estimado:",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = when (currentStatus) {
                                OrderStatus.RECEIVED -> "25-35 min"
                                OrderStatus.PREPARING -> "20-30 min"
                                OrderStatus.ON_THE_WAY -> "10-15 min"
                                OrderStatus.DELIVERED -> "Entregado"
                            },
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Purple500
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Repartidor:",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = "Carlos M.",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Botón para volver al inicio
            Button(
                onClick = { 
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Purple500),
                shape = RoundedCornerShape(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Inicio",
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Volver al Inicio",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
fun OrderStatusStepper(currentStatus: OrderStatus) {
    val steps = listOf(
        Triple("Recibido", "Tu pedido ha sido recibido", Icons.Default.Check),
        Triple("Preparando", "Estamos preparando tu pedido", Icons.Default.Restaurant),
        Triple("En camino", "Tu pedido está en camino", Icons.Default.LocalShipping),
        Triple("Entregado", "¡Tu pedido ha llegado!", Icons.Default.Home)
    )
    
    Column(
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        steps.forEachIndexed { index, (title, description, icon) ->
            val isCompleted = when (currentStatus) {
                OrderStatus.RECEIVED -> index == 0
                OrderStatus.PREPARING -> index <= 1
                OrderStatus.ON_THE_WAY -> index <= 2
                OrderStatus.DELIVERED -> index <= 3
            }
            
            val isCurrent = when (currentStatus) {
                OrderStatus.RECEIVED -> index == 0
                OrderStatus.PREPARING -> index == 1
                OrderStatus.ON_THE_WAY -> index == 2
                OrderStatus.DELIVERED -> index == 3
            }
            
            Row(
                verticalAlignment = Alignment.Top
            ) {
                // Círculo del estado
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCompleted -> Purple500
                                isCurrent -> Purple500.copy(alpha = 0.3f)
                                else -> Color.LightGray
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isCompleted) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    } else if (isCurrent) {
                        Icon(
                            imageVector = icon,
                            contentDescription = title,
                            tint = Purple500,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        Text(
                            text = (index + 1).toString(),
                            color = Color.Gray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                // Información del estado
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (isCompleted || isCurrent) Color.Black else Color.Gray
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = description,
                        fontSize = 14.sp,
                        color = if (isCompleted || isCurrent) Color.Gray else Color.LightGray
                    )
                }
            }
            
            // Línea conectora (excepto para el último elemento)
            if (index < steps.size - 1) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(24.dp)
                        .background(
                            if (isCompleted) Purple500 else Color.LightGray,
                            RoundedCornerShape(1.dp)
                        )
                        .align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

