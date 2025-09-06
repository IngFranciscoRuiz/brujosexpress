package com.fjapps.brujosexpress.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fjapps.brujosexpress.admin.ui.theme.Purple500

private val Orange = Color(0xFFFF9800)
private val Purple = Color(0xFF9C27B0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboard() {
    var orders by remember { mutableStateOf(generateSampleOrders()) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Brujos Express Admin",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Purple500,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Panel de Administración",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                
                Text(
                    text = "Gestiona pedidos y actualiza estados",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(bottom = 24.dp)
                )
            }
            
            items(orders) { order ->
                OrderCard(
                    order = order,
                    onStatusChange = { newStatus ->
                        orders = orders.map { 
                            if (it.id == order.id) it.copy(status = newStatus) else it 
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun OrderCard(
    order: AdminOrder,
    onStatusChange: (OrderStatus) -> Unit
) {
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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Pedido #${order.id}",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                
                StatusChip(status = order.status)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Información del cliente
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Cliente",
                    tint = Purple500,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = order.customerName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Dirección
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Dirección",
                    tint = Purple500,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = order.deliveryAddress,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Total
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.AttachMoney,
                    contentDescription = "Total",
                    tint = Purple500,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                Text(
                    text = "$${String.format("%.2f", order.total)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Purple500
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Botones de cambio de estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OrderStatus.values().forEach { status ->
                    Button(
                        onClick = { onStatusChange(status) },
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (order.status == status) Purple500 else Color.LightGray
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = status.displayName,
                            fontSize = 12.sp,
                            color = if (order.status == status) Color.White else Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusChip(status: OrderStatus) {
    val backgroundColor = when (status) {
        OrderStatus.RECEIVED -> Color.Blue
        OrderStatus.PREPARING -> Orange
        OrderStatus.ON_THE_WAY -> Purple
        OrderStatus.DELIVERED -> Color.Green
    }
    
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = backgroundColor,
        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.displayName,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}

// Modelos de datos para la app admin
data class AdminOrder(
    val id: String,
    val customerName: String,
    val deliveryAddress: String,
    val total: Double,
    val status: OrderStatus,
    val orderDate: String
)

enum class OrderStatus(val displayName: String) {
    RECEIVED("Recibido"),
    PREPARING("Preparando"),
    ON_THE_WAY("En Camino"),
    DELIVERED("Entregado")
}

fun generateSampleOrders(): List<AdminOrder> {
    return listOf(
        AdminOrder(
            id = "BE-001",
            customerName = "María González",
            deliveryAddress = "Calle Principal 123, Centro",
            total = 45.99,
            status = OrderStatus.RECEIVED,
            orderDate = "2024-01-15 14:30"
        ),
        AdminOrder(
            id = "BE-002",
            customerName = "Carlos Rodríguez",
            deliveryAddress = "Av. Libertad 456, Norte",
            total = 32.50,
            status = OrderStatus.PREPARING,
            orderDate = "2024-01-15 15:15"
        ),
        AdminOrder(
            id = "BE-003",
            customerName = "Ana Martínez",
            deliveryAddress = "Calle 5 de Mayo 789, Sur",
            total = 67.25,
            status = OrderStatus.ON_THE_WAY,
            orderDate = "2024-01-15 13:45"
        ),
        AdminOrder(
            id = "BE-004",
            customerName = "Luis Pérez",
            deliveryAddress = "Blvd. Independencia 321, Este",
            total = 28.75,
            status = OrderStatus.DELIVERED,
            orderDate = "2024-01-15 12:20"
        )
    )
}

