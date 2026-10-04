package com.example.kart.feature.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.kart.core.ui.AppViewModelProvider
import com.example.kart.feature.cart.CartScreen
import com.example.kart.feature.cart.CartViewModel
import com.example.kart.feature.product.details.ProductDetailsScreen
import com.example.kart.feature.product.details.ProductDetailsViewModel
import com.example.kart.feature.product.list.ProductListScreen
import com.example.kart.feature.product.list.ProductListViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val cartViewModel: CartViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val cartUiState by cartViewModel.cartUiState.collectAsStateWithLifecycle()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Home, contentDescription = "Products") },
                    label = { Text("Products") },
                    selected = currentDestination?.hierarchy?.any { it.route == "products" } == true,
                    onClick = {
                        navController.navigate("products") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )

                NavigationBarItem(
                    icon = {
                        BadgedBox(
                            badge = {
                                if (cartUiState.totalCount > 0) {
                                    Badge { Text("${cartUiState.totalCount}") }
                                }
                            }
                        ) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = "Cart")
                        }
                    },
                    label = { Text("Cart") },
                    selected = currentDestination?.hierarchy?.any { it.route == "cart" } == true,
                    onClick = {
                        navController.navigate("cart") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "products",
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable("products") {
                val viewModel: ProductListViewModel = viewModel(factory = AppViewModelProvider.Factory)
                ProductListScreen(
                    viewModel = viewModel,
                    onProductClick = { productId ->
                        navController.navigate("product_details/$productId")
                    }
                )
            }
            composable(
                route = "product_details/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getInt("productId") ?: return@composable
                val viewModel: ProductDetailsViewModel = viewModel(factory = AppViewModelProvider.Factory)
                ProductDetailsScreen(
                    productId = productId,
                    viewModel = viewModel,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToCart = {
                        navController.navigate("cart") {
                            popUpTo("products")
                        }
                    }
                )
            }
            composable("cart") {
                CartScreen(
                    viewModel = cartViewModel,
                    onContinueShopping = {
                        navController.navigate("products") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}
