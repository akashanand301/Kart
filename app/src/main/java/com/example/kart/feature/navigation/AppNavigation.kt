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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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
            CustomBottomBar(
                currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route,
                cartCount = cartUiState.totalCount,
                onNavigate = { route ->
                    navController.navigate(route) {
                        popUpTo("products") {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
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
                        navController.popBackStack("products", inclusive = false)
                    }
                )
            }
        }
    }
}

@Composable
fun CustomBottomBar(
    currentRoute: String?,
    cartCount: Int,
    onNavigate: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 16.dp)
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(32.dp),
        color = MaterialTheme.colorScheme.surfaceVariant,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val isHome = currentRoute?.contains("products") == true || currentRoute?.contains("product_details") == true
            val isCart = currentRoute == "cart"
            
            NavBarItem(
                icon = Icons.Filled.Home,
                label = "Home",
                selected = isHome,
                onClick = { onNavigate("products") }
            )
            
            NavBarItem(
                icon = Icons.Filled.ShoppingCart,
                label = "Cart",
                selected = isCart,
                badgeCount = cartCount,
                onClick = { onNavigate("cart") }
            )
        }
    }
}

@Composable
fun NavBarItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    selected: Boolean,
    badgeCount: Int = 0,
    onClick: () -> Unit
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
        label = "nav_bg"
    )
    val contentColor by animateColorAsState(
        targetValue = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        label = "nav_content"
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(24.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BadgedBox(
                badge = {
                    if (badgeCount > 0) {
                        Badge { Text("$badgeCount") }
                    }
                }
            ) {
                Icon(icon, contentDescription = label, tint = contentColor)
            }
            AnimatedVisibility(visible = selected) {
                Row {
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(label, color = contentColor, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}
