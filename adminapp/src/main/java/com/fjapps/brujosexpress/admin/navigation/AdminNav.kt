package com.fjapps.brujosexpress.admin.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.fjapps.brujosexpress.admin.ui.dashboard.DashboardScreen
import com.fjapps.brujosexpress.admin.ui.login.LoginScreen
import com.fjapps.brujosexpress.admin.ui.orders.OrdersScreen
import com.fjapps.brujosexpress.admin.ui.stores.CreateStoreScreen
import com.fjapps.brujosexpress.admin.ui.products.ProductEditScreen
import com.fjapps.brujosexpress.admin.ui.products.ProductsScreen
import com.fjapps.brujosexpress.admin.ui.settings.SettingsScreen

sealed class AdminRoute(val route: String) {
    data object Login : AdminRoute("login")
    data object Dashboard : AdminRoute("dashboard")
    data object Orders : AdminRoute("orders")
    data object OrderDetail : AdminRoute("order_detail/{orderId}")
    data object Products : AdminRoute("products")
    data object ProductEdit : AdminRoute("product_edit?productId={id}")
    data object Settings : AdminRoute("settings")
    data object CreateStore : AdminRoute("create_store")
}

@Composable
fun AdminNavRoot() {
    val navController = rememberNavController()
    Scaffold { padding ->
        NavHost(
            navController = navController,
            startDestination = AdminRoute.Login.route
        ) {
            composable(AdminRoute.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        navController.navigate(AdminRoute.Dashboard.route) {
                            popUpTo(AdminRoute.Login.route) { inclusive = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
            composable(AdminRoute.Dashboard.route) {
                DashboardScreen(
                    onOpenProducts = { navController.navigate(AdminRoute.Products.route) },
                    onOpenOrders = { navController.navigate(AdminRoute.Orders.route) },
                    onOpenSettings = { navController.navigate(AdminRoute.Settings.route) },
                    onOpenCreateStore = { navController.navigate(AdminRoute.CreateStore.route) }
                )
            }
            composable(AdminRoute.Products.route) {
                ProductsScreen(navController)
            }
            composable(
                route = "product_edit?productId={id}",
                arguments = listOf(navArgument("id") { type = NavType.StringType; nullable = true })
            ) { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                ProductEditScreen(navController, productId = id)
            }
            composable(AdminRoute.Settings.route) {
                SettingsScreen(navController)
            }
            composable(AdminRoute.CreateStore.route) {
                CreateStoreScreen(navController)
            }
            // Placeholders for others
            composable(AdminRoute.Orders.route) { OrdersScreen(navController) }
            composable("order_detail/{orderId}") { backStackEntry ->
                Text("Order detail: ${backStackEntry.arguments?.getString("orderId")}")
            }
        }
    }
}


