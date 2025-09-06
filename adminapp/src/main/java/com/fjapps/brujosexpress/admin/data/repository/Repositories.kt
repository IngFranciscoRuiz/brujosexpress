package com.fjapps.brujosexpress.admin.data.repository

import com.fjapps.brujosexpress.admin.ui.model.AdminOrder
import com.fjapps.brujosexpress.admin.ui.model.AdminProduct
import com.fjapps.brujosexpress.admin.ui.model.AdminStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID

// Interfaces
interface AuthRepository {
    suspend fun login(email: String, password: String): Boolean
    suspend fun logout()
}

interface ProductsRepository {
    val products: Flow<List<AdminProduct>>
    suspend fun upsert(product: AdminProduct)
    suspend fun duplicate(productId: String)
    suspend fun delete(productId: String)
}

interface OrdersRepository {
    val orders: Flow<List<AdminOrder>>
    suspend fun updateStatus(orderId: String, status: String)
}

interface StoreRepository {
    val store: Flow<AdminStore>
    suspend fun updateStore(store: AdminStore)
}

// In-memory implementations (swap later for Firebase)
class InMemoryAuthRepository : AuthRepository {
    override suspend fun login(email: String, password: String): Boolean = email.isNotBlank() && password.isNotBlank()
    override suspend fun logout() {}
}

class InMemoryProductsRepository : ProductsRepository {
    private val state = MutableStateFlow(
        listOf(
            AdminProduct("1", "Hamburguesa Clásica", 12900, null, "Comida", true),
            AdminProduct("2", "Pizza Pepperoni", 18900, null, "Comida", true),
            AdminProduct("3", "Refresco Cola 600ml", 2900, null, "Bebidas", true),
            AdminProduct("4", "Agua 1L", 1900, null, "Bebidas", false)
        )
    )
    override val products: Flow<List<AdminProduct>> = state.asStateFlow()
    override suspend fun upsert(product: AdminProduct) {
        val current = state.value.toMutableList()
        val idx = current.indexOfFirst { it.id == product.id }
        if (idx >= 0) current[idx] = product else current.add(product.copy(id = UUID.randomUUID().toString()))
        state.value = current
    }
    override suspend fun duplicate(productId: String) {
        val current = state.value.toMutableList()
        current.find { it.id == productId }?.let { p ->
            current.add(p.copy(id = UUID.randomUUID().toString(), name = p.name + " (Copia)"))
            state.value = current
        }
    }
    override suspend fun delete(productId: String) {
        state.value = state.value.filterNot { it.id == productId }
    }
}

class InMemoryOrdersRepository : OrdersRepository {
    private val state = MutableStateFlow(
        listOf(
            AdminOrder("BE-1001", "María", 4599, "Recibido", 1725600000000),
            AdminOrder("BE-1002", "Carlos", 3299, "Preparando", 1725603600000),
            AdminOrder("BE-1003", "Ana", 6725, "En camino", 1725607200000)
        )
    )
    override val orders: Flow<List<AdminOrder>> = state.asStateFlow()
    override suspend fun updateStatus(orderId: String, status: String) {
        state.value = state.value.map { if (it.id == orderId) it.copy(status = status) else it }
    }
}

class InMemoryStoreRepository : StoreRepository {
    private val state = MutableStateFlow(
        AdminStore("1", "Brujos Express", null, "+52 55 1234 5678", 2500, true)
    )
    override val store: Flow<AdminStore> = state.asStateFlow()
    override suspend fun updateStore(store: AdminStore) { state.value = store }
}


