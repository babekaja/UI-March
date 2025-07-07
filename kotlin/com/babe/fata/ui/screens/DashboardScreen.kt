package com.babe.fata.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowDpSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.babe.fata.ui.components.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSize = currentWindowDpSize()
    val isLargeScreen = windowSize.width > 1200.dp
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // En-tête
        Text(
            text = "Tableau de bord",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        // Métriques principales
        if (isLargeScreen) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(200.dp)
            ) {
                items(getMetrics()) { metric ->
                    MetricCard(metric = metric)
                }
            }
        } else {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.height(120.dp)
            ) {
                items(getMetrics()) { metric ->
                    MetricCard(
                        metric = metric,
                        modifier = Modifier.width(200.dp)
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Alertes
        AlertsSection()
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Produits en rupture
        LowStockSection(onProductClick = onProductClick)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Mouvements récents
        RecentMovementsSection()
    }
}

@Composable
private fun MetricCard(
    metric: DashboardMetric,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = metric.icon,
                    contentDescription = null,
                    tint = metric.color
                )
                Text(
                    text = metric.title,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = metric.value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )
            if (metric.change != null) {
                Text(
                    text = metric.change,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (metric.change.startsWith("+")) 
                        MaterialTheme.colorScheme.primary 
                    else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun AlertsSection() {
    Column {
        Text(
            text = "Alertes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyColumn(
            modifier = Modifier.height(200.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getAlerts()) { alert ->
                AlertCard(alert = alert)
            }
        }
    }
}

@Composable
private fun AlertCard(alert: Alert) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = when (alert.severity) {
                AlertSeverity.HIGH -> MaterialTheme.colorScheme.errorContainer
                AlertSeverity.MEDIUM -> MaterialTheme.colorScheme.secondaryContainer
                AlertSeverity.LOW -> MaterialTheme.colorScheme.surfaceVariant
            }
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = alert.icon,
                contentDescription = null,
                tint = when (alert.severity) {
                    AlertSeverity.HIGH -> MaterialTheme.colorScheme.error
                    AlertSeverity.MEDIUM -> MaterialTheme.colorScheme.secondary
                    AlertSeverity.LOW -> MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = alert.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = alert.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun LowStockSection(onProductClick: (Product) -> Unit) {
    Column {
        Text(
            text = "Produits en rupture",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(getLowStockProducts()) { product ->
                ProductCard(
                    product = product,
                    onClick = { onProductClick(product) },
                    modifier = Modifier.width(200.dp)
                )
            }
        }
    }
}

@Composable
private fun RecentMovementsSection() {
    Column {
        Text(
            text = "Mouvements récents",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        LazyColumn(
            modifier = Modifier.height(300.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(getRecentMovements()) { movement ->
                MovementCard(movement = movement)
            }
        }
    }
}

// Data classes et fonctions utilitaires
data class DashboardMetric(
    val title: String,
    val value: String,
    val change: String?,
    val icon: ImageVector,
    val color: androidx.compose.ui.graphics.Color
)

data class Alert(
    val title: String,
    val description: String,
    val severity: AlertSeverity,
    val icon: ImageVector
)

enum class AlertSeverity { HIGH, MEDIUM, LOW }

@Composable
private fun getMetrics(): List<DashboardMetric> = listOf(
    DashboardMetric(
        title = "Produits totaux",
        value = "1,247",
        change = "+12 ce mois",
        icon = Icons.Default.Inventory,
        color = MaterialTheme.colorScheme.primary
    ),
    DashboardMetric(
        title = "Valeur du stock",
        value = "€45,230",
        change = "+5.2%",
        icon = Icons.Default.Euro,
        color = MaterialTheme.colorScheme.secondary
    ),
    DashboardMetric(
        title = "Ruptures",
        value = "23",
        change = "-3 aujourd'hui",
        icon = Icons.Default.Warning,
        color = MaterialTheme.colorScheme.error
    ),
    DashboardMetric(
        title = "Commandes",
        value = "156",
        change = "+8 cette semaine",
        icon = Icons.Default.ShoppingCart,
        color = MaterialTheme.colorScheme.tertiary
    )
)

@Composable
private fun getAlerts(): List<Alert> = listOf(
    Alert(
        title = "Stock critique",
        description = "15 produits en dessous du seuil minimum",
        severity = AlertSeverity.HIGH,
        icon = Icons.Default.Warning
    ),
    Alert(
        title = "Commande en retard",
        description = "3 commandes fournisseurs en retard",
        severity = AlertSeverity.MEDIUM,
        icon = Icons.Default.Schedule
    ),
    Alert(
        title = "Inventaire programmé",
        description = "Inventaire mensuel prévu demain",
        severity = AlertSeverity.LOW,
        icon = Icons.Default.EventNote
    )
)

@Composable
private fun getLowStockProducts(): List<Product> = listOf(
    Product(1, "Ordinateur portable Dell", "DELL-001", 5, 10, "€899.99"),
    Product(2, "Souris sans fil", "MOUSE-001", 2, 20, "€29.99"),
    Product(3, "Clavier mécanique", "KEY-001", 1, 15, "€129.99")
)

@Composable
private fun getRecentMovements(): List<StockMovement> = listOf(
    StockMovement(1, "Entrée", "Ordinateur portable Dell", 10, "Aujourd'hui 14:30"),
    StockMovement(2, "Sortie", "Souris sans fil", -5, "Aujourd'hui 12:15"),
    StockMovement(3, "Entrée", "Clavier mécanique", 20, "Hier 16:45")
)

// Composants réutilisables
@Composable
private fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Stock: ${product.currentStock}/${product.minStock}",
                style = MaterialTheme.typography.bodySmall,
                color = if (product.currentStock <= product.minStock) 
                    MaterialTheme.colorScheme.error 
                else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = product.price,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun MovementCard(movement: StockMovement) {
    Card {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = if (movement.quantity > 0) Icons.Default.Add else Icons.Default.Remove,
                contentDescription = null,
                tint = if (movement.quantity > 0) 
                    MaterialTheme.colorScheme.primary 
                else MaterialTheme.colorScheme.error
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = movement.productName,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${movement.type} • ${movement.timestamp}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Text(
                text = "${if (movement.quantity > 0) "+" else ""}${movement.quantity}",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = if (movement.quantity > 0) 
                    MaterialTheme.colorScheme.primary 
                else MaterialTheme.colorScheme.error
            )
        }
    }
}

data class StockMovement(
    val id: Int,
    val type: String,
    val productName: String,
    val quantity: Int,
    val timestamp: String
)