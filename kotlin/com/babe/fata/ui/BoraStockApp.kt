package com.babe.fata.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.*
import androidx.compose.material3.adaptive.layout.*
import androidx.compose.material3.adaptive.navigation.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.babe.fata.ui.components.*
import com.babe.fata.ui.screens.*

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun BoraStockApp() {
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()
    val windowSize = currentWindowDpSize()
    val posture = calculatePosture(windowAdaptiveInfo)
    
    var currentDestination by remember { mutableStateOf(BoraStockDestination.DASHBOARD) }
    var selectedProduct by remember { mutableStateOf<Product?>(null) }
    var selectedSupplier by remember { mutableStateOf<Supplier?>(null) }
    var selectedMovement by remember { mutableStateOf<StockMovement?>(null) }
    
    val navigator = rememberThreePaneScaffoldNavigator(
        scaffoldDirective = calculateThreePaneScaffoldDirective(
            windowAdaptiveInfo,
            ThreePaneScaffoldDefaults.adaptStrategies()
        )
    )

    ThreePaneScaffold(
        modifier = Modifier.fillMaxSize(),
        scaffoldDirective = navigator.scaffoldDirective,
        value = navigator.scaffoldValue,
        primaryPane = {
            AnimatedPane {
                NavigationPane(
                    currentDestination = currentDestination,
                    onDestinationSelected = { destination ->
                        currentDestination = destination
                        // Reset selections when changing sections
                        selectedProduct = null
                        selectedSupplier = null
                        selectedMovement = null
                    },
                    windowSize = windowSize
                )
            }
        },
        secondaryPane = {
            AnimatedPane {
                when (currentDestination) {
                    BoraStockDestination.DASHBOARD -> {
                        DashboardScreen(
                            onProductClick = { product ->
                                selectedProduct = product
                                navigator.navigateTo(ThreePaneScaffoldRole.Tertiary)
                            }
                        )
                    }
                    BoraStockDestination.PRODUCTS -> {
                        ProductListScreen(
                            onProductClick = { product ->
                                selectedProduct = product
                                navigator.navigateTo(ThreePaneScaffoldRole.Tertiary)
                            }
                        )
                    }
                    BoraStockDestination.MOVEMENTS -> {
                        MovementsScreen(
                            onMovementClick = { movement ->
                                selectedMovement = movement
                                navigator.navigateTo(ThreePaneScaffoldRole.Tertiary)
                            }
                        )
                    }
                    BoraStockDestination.SUPPLIERS -> {
                        SuppliersScreen(
                            onSupplierClick = { supplier ->
                                selectedSupplier = supplier
                                navigator.navigateTo(ThreePaneScaffoldRole.Tertiary)
                            }
                        )
                    }
                }
            }
        },
        tertiaryPane = {
            AnimatedPane {
                when (currentDestination) {
                    BoraStockDestination.DASHBOARD -> {
                        selectedProduct?.let { product ->
                            ProductDetailScreen(
                                product = product,
                                onBack = { navigator.navigateBack() }
                            )
                        } ?: EmptyDetailPane("Sélectionnez un produit")
                    }
                    BoraStockDestination.PRODUCTS -> {
                        selectedProduct?.let { product ->
                            ProductDetailScreen(
                                product = product,
                                onBack = { navigator.navigateBack() }
                            )
                        } ?: EmptyDetailPane("Sélectionnez un produit")
                    }
                    BoraStockDestination.MOVEMENTS -> {
                        selectedMovement?.let { movement ->
                            MovementDetailScreen(
                                movement = movement,
                                onBack = { navigator.navigateBack() }
                            )
                        } ?: EmptyDetailPane("Sélectionnez un mouvement")
                    }
                    BoraStockDestination.SUPPLIERS -> {
                        selectedSupplier?.let { supplier ->
                            SupplierDetailScreen(
                                supplier = supplier,
                                onBack = { navigator.navigateBack() }
                            )
                        } ?: EmptyDetailPane("Sélectionnez un fournisseur")
                    }
                }
            }
        }
    )
}

enum class BoraStockDestination(
    val label: String,
    val icon: ImageVector
) {
    DASHBOARD("Tableau de bord", Icons.Default.Dashboard),
    PRODUCTS("Produits", Icons.Default.Inventory),
    MOVEMENTS("Mouvements", Icons.Default.SwapHoriz),
    SUPPLIERS("Fournisseurs", Icons.Default.Business)
}

@Composable
private fun EmptyDetailPane(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}