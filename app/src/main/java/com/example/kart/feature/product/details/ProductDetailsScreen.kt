package com.example.kart.feature.product.details

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.kart.core.common.UiState
import com.example.kart.domain.model.Product
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.Crossfade

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    productId: Int,
    viewModel: ProductDetailsViewModel,
    onBackClick: () -> Unit,
    onNavigateToCart: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val addedToCart by viewModel.addedToCart.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    LaunchedEffect(addedToCart) {
        if (addedToCart) {
            Toast.makeText(context, "Added to cart", Toast.LENGTH_SHORT).show()
            viewModel.resetAddedToCart()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    val titleText = (uiState as? UiState.Success)?.data?.title ?: ""
                    Text(titleText) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding).fillMaxSize()) {
            when (val state = uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Success -> {
                    val cartItem by viewModel.observeCartItem(productId).collectAsStateWithLifecycle(null)
                    ProductDetailsContent(
                        product = state.data,
                        quantityInCart = cartItem?.quantity ?: 0,
                        onAddToCart = { viewModel.addToCart(state.data) },
                        onIncrease = { viewModel.increaseQuantity(state.data.id) },
                        onDecrease = { viewModel.decreaseQuantity(state.data.id) },
                        onGoToCart = onNavigateToCart
                    )
                }
                is UiState.Error -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = state.message, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadProduct(productId) }) {
                            Text("Retry")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductDetailsContent(
    product: Product,
    quantityInCart: Int,
    onAddToCart: () -> Unit,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onGoToCart: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
        ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            AsyncImage(
                model = product.thumbnail,
                contentDescription = product.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(16.dp),
                contentScale = ContentScale.Fit
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = product.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "₹${product.price}",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.ExtraBold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoCard(modifier = Modifier.weight(1f), label = "Rating", value = "${product.rating} ⭐")
            InfoCard(modifier = Modifier.weight(1f), label = "Stock", value = "${product.stock}")
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            InfoCard(modifier = Modifier.weight(1f), label = "Brand", value = product.brand)
            InfoCard(modifier = Modifier.weight(1f), label = "Category", value = product.category.capitalize())
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Description",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.secondary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = product.description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        } // End of scrollable Column
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Box(modifier = Modifier.fillMaxWidth().height(56.dp)) {
            Crossfade(
                targetState = quantityInCart > 0, 
                label = "AddToCartToggle",
                modifier = Modifier.fillMaxSize()
            ) { isAdded ->
                if (isAdded) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            FilledIconButton(onClick = onDecrease, modifier = Modifier.size(48.dp)) {
                                Icon(Icons.Filled.Remove, contentDescription = "Decrease")
                            }
                            Text(
                                text = "$quantityInCart",
                                modifier = Modifier.padding(horizontal = 24.dp),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold
                            )
                            FilledIconButton(onClick = onIncrease, modifier = Modifier.size(48.dp)) {
                                Icon(Icons.Filled.Add, contentDescription = "Increase")
                            }
                        }
                        
                        Button(
                            onClick = onGoToCart,
                            modifier = Modifier.height(56.dp)
                        ) {
                            Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("CART")
                        }
                    }
                } else {
                    Button(
                        onClick = onAddToCart,
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Filled.ShoppingCart, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("ADD TO CART", style = MaterialTheme.typography.titleMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun InfoCard(modifier: Modifier = Modifier, label: String, value: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value, 
                style = MaterialTheme.typography.titleMedium, 
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1
            )
        }
    }
}
