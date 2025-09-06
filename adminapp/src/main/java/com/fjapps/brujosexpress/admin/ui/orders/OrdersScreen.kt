package com.fjapps.brujosexpress.admin.ui.orders

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fjapps.brujosexpress.admin.data.di.rememberOrdersViewModel

data class AdminOrder(
    val id: String,
    val customer: String,
    val totalCents: Int,
    val status: String,
    val timestamp: Long
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersScreen(navController: NavController) {
    val vm = rememberOrdersViewModel()
    val orders = vm.orders.collectAsState().value
    val allStatuses = listOf("Todos", "Recibido", "Preparando", "En camino", "Entregado")
    var selectedFilter by remember { mutableStateOf("Todos") }
    var query by remember { mutableStateOf("") }
    var sortBy by remember { mutableStateOf("Fecha (recientes)") }

    Scaffold(topBar = { TopAppBar(title = { Text("Pedidos") }) }) { padding ->
        if (orders.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("Sin pedidos por ahora")
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                // Buscador y ordenamiento
                OutlinedTextField(
                    value = query,
                    onValueChange = { query = it },
                    label = { Text("Buscar por ID o cliente") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                var sortMenuExpanded by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(expanded = sortMenuExpanded, onExpandedChange = { sortMenuExpanded = !sortMenuExpanded }) {
                    OutlinedTextField(
                        value = sortBy,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ordenar por") },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )
                    ExposedDropdownMenu(expanded = sortMenuExpanded, onDismissRequest = { sortMenuExpanded = false }) {
                        listOf("Fecha (recientes)", "Fecha (antiguos)", "Estado").forEach { option ->
                            DropdownMenuItem(text = { Text(option) }, onClick = {
                                sortBy = option
                                sortMenuExpanded = false
                            })
                        }
                    }
                }
                Spacer(Modifier.height(12.dp))
                // Filtros de estado
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    allStatuses.forEach { status ->
                        val active = selectedFilter == status
                        FilterChip(
                            selected = active,
                            onClick = { selectedFilter = status },
                            label = { Text(status) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = MaterialTheme.colorScheme.primary,
                                selectedBorderColor = MaterialTheme.colorScheme.primary
                            )
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))

                val filtered = orders.filter { o ->
                    (selectedFilter == "Todos" || o.status == selectedFilter) &&
                    (query.isBlank() || o.id.contains(query, true) || o.customer.contains(query, true))
                }.let { list ->
                    when (sortBy) {
                        "Fecha (antiguos)" -> list.sortedBy { it.timestamp }
                        "Estado" -> list.sortedBy { it.status }
                        else -> list.sortedByDescending { it.timestamp }
                    }
                }

                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
                    items(filtered) { order ->
                        Card(shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(2.dp)) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Pedido ${order.id}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                                    Text(order.customer)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Text("$${order.totalCents / 100.0}", fontWeight = FontWeight.Bold)
                                    AssistChip(
                                        onClick = {
                                            val next = nextStatus(order.status)
                                            vm.updateStatus(order.id, next)
                                        },
                                        label = { Text(order.status) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun nextStatus(current: String): String {
    val statuses = listOf("Recibido", "Preparando", "En camino", "Entregado")
    val idx = statuses.indexOf(current)
    return if (idx == -1 || idx == statuses.lastIndex) statuses.first() else statuses[idx + 1]
}


