package com.fjapps.brujosexpress.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Explore
import com.fjapps.brujosexpress.ui.screens.*

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(navController)
        }
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }
        composable(Screen.RestaurantCatalog.route) {
            RestaurantCatalogScreen(navController)
        }
        composable(Screen.GroceryCatalog.route) {
            GroceryCatalogScreen(navController)
        }
        composable(Screen.Cart.route) {
            CartScreen(navController)
        }
        composable(Screen.Search.route) { SimplePlaceholder("Buscar") }
        composable(Screen.Orders.route) { SimplePlaceholder("Pedidos") }
        composable(Screen.Profile.route) { SimplePlaceholder("Perfil") }
        composable(Screen.Summary.route) {
            OrderSummaryScreen(navController)
        }
        composable(Screen.Tracking.route) {
            OrderTrackingScreen(navController)
        }
    }
}

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object Home : Screen("home")
    object RestaurantCatalog : Screen("restaurantCatalog")
    object GroceryCatalog : Screen("groceryCatalog")
    object Cart : Screen("cart")
    object Search : Screen("search")
    object Orders : Screen("orders")
    object Profile : Screen("profile")
    object Summary : Screen("summary")
    object Tracking : Screen("tracking")
}

@Composable
fun BottomNavBar(navController: androidx.navigation.NavController) {
    val items = listOf(
        Screen.Home to Icons.Default.Home,
        Screen.RestaurantCatalog to Icons.Default.Explore,
        Screen.Search to Icons.Default.Search,
        Screen.Orders to Icons.Default.ListAlt,
        Screen.Profile to Icons.Default.Person
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        items.forEach { (screen, icon) ->
            NavigationBarItem(
                selected = currentRoute == screen.route,
                onClick = {
                    if (currentRoute != screen.route) {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                    }
                },
                icon = { Icon(icon, contentDescription = screen.route) },
                label = { Text(screen.route.replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}

@Composable
fun SimplePlaceholder(title: String) {
    Text(text = title)
}

