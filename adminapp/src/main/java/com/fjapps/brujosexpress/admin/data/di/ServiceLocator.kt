package com.fjapps.brujosexpress.admin.data.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.fjapps.brujosexpress.admin.data.repository.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.fjapps.brujosexpress.admin.viewmodel.LoginViewModel
import com.fjapps.brujosexpress.admin.viewmodel.OrdersViewModel
import com.fjapps.brujosexpress.admin.viewmodel.ProductsViewModel
import com.fjapps.brujosexpress.admin.viewmodel.SettingsViewModel

object ServiceLocator {
    private const val DEFAULT_STORE_ID = "default-store" // fallback si no hay seleccionado
    private val firestore by lazy { FirebaseFirestore.getInstance() }
    private val storage by lazy { FirebaseStorage.getInstance() }

    val auth by lazy { InMemoryAuthRepository() }
    private fun storeId(): String = StoreManager.currentStoreId ?: DEFAULT_STORE_ID
    val products by lazy { FirestoreProductsRepository(firestore, storage, storeId()) }
    val orders by lazy { FirestoreOrdersRepository(firestore, storeId()) }
    val store by lazy { FirestoreStoreRepository(firestore, storeId()) }
}

@Composable
fun rememberLoginViewModel(): LoginViewModel = remember { LoginViewModel(ServiceLocator.auth) }

@Composable
fun rememberProductsViewModel(): ProductsViewModel = remember { ProductsViewModel(ServiceLocator.products) }

@Composable
fun rememberOrdersViewModel(): OrdersViewModel = remember { OrdersViewModel(ServiceLocator.orders) }

@Composable
fun rememberSettingsViewModel(): SettingsViewModel = remember { SettingsViewModel(ServiceLocator.store) }


