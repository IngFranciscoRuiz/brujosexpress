package com.fjapps.brujosexpress.admin.data.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.fjapps.brujosexpress.admin.data.repository.InMemoryAuthRepository
import com.fjapps.brujosexpress.admin.data.repository.InMemoryOrdersRepository
import com.fjapps.brujosexpress.admin.data.repository.InMemoryProductsRepository
import com.fjapps.brujosexpress.admin.data.repository.InMemoryStoreRepository
import com.fjapps.brujosexpress.admin.viewmodel.LoginViewModel
import com.fjapps.brujosexpress.admin.viewmodel.OrdersViewModel
import com.fjapps.brujosexpress.admin.viewmodel.ProductsViewModel
import com.fjapps.brujosexpress.admin.viewmodel.SettingsViewModel

object ServiceLocator {
    // Single instances for in-memory repos
    val auth by lazy { InMemoryAuthRepository() }
    val products by lazy { InMemoryProductsRepository() }
    val orders by lazy { InMemoryOrdersRepository() }
    val store by lazy { InMemoryStoreRepository() }
}

@Composable
fun rememberLoginViewModel(): LoginViewModel = remember { LoginViewModel(ServiceLocator.auth) }

@Composable
fun rememberProductsViewModel(): ProductsViewModel = remember { ProductsViewModel(ServiceLocator.products) }

@Composable
fun rememberOrdersViewModel(): OrdersViewModel = remember { OrdersViewModel(ServiceLocator.orders) }

@Composable
fun rememberSettingsViewModel(): SettingsViewModel = remember { SettingsViewModel(ServiceLocator.store) }


