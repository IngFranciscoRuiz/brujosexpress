package com.fjapps.brujosexpress.admin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fjapps.brujosexpress.admin.data.repository.*
import com.fjapps.brujosexpress.admin.ui.model.AdminOrder
import com.fjapps.brujosexpress.admin.ui.model.AdminProduct
import com.fjapps.brujosexpress.admin.ui.model.AdminStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AdminDeps(
    val auth: AuthRepository = InMemoryAuthRepository(),
    val products: ProductsRepository = InMemoryProductsRepository(),
    val orders: OrdersRepository = InMemoryOrdersRepository(),
    val store: StoreRepository = InMemoryStoreRepository()
)

class LoginViewModel(private val auth: AuthRepository) : ViewModel() {
    val loading = MutableStateFlow(false)
    val error = MutableStateFlow<String?>(null)
    fun login(email: String, password: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            loading.value = true
            error.value = null
            val ok = auth.login(email, password)
            loading.value = false
            if (ok) onSuccess() else error.value = "Credenciales inválidas"
        }
    }
}

class ProductsViewModel(private val repo: ProductsRepository) : ViewModel() {
    val products: StateFlow<List<AdminProduct>> = repo.products.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    fun upsert(product: AdminProduct) = viewModelScope.launch { repo.upsert(product) }
    fun duplicate(productId: String) = viewModelScope.launch { repo.duplicate(productId) }
    fun delete(productId: String) = viewModelScope.launch { repo.delete(productId) }
}

class OrdersViewModel(private val repo: OrdersRepository) : ViewModel() {
    val orders: StateFlow<List<AdminOrder>> = repo.orders.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
    fun updateStatus(orderId: String, status: String) = viewModelScope.launch { repo.updateStatus(orderId, status) }
}

class SettingsViewModel(private val repo: StoreRepository) : ViewModel() {
    val store: StateFlow<AdminStore> = repo.store.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), AdminStore("","", null, null, 0, false))
    fun updateStore(store: AdminStore) = viewModelScope.launch { repo.updateStore(store) }
}


