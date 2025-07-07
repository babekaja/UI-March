package com.babe.fata.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowDpSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.babe.fata.ui.components.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    product: Product,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSize = currentWindowDpSize()
    val isCompact = windowSize.width < 600.dp
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // En-tête avec bouton retour (seulement sur écrans compacts)
        if (isCompact) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Retour")
                }
                Text(
                    text = "Détails du produit",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Text(
                text = "Détails du produit",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
        
        // Informations principales
        ProductInfoCard(product = product)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Statistiques de stock
        StockStatsCard(product = product)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Actions rapides
        QuickActionsCard(product = product)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Historique des mouvements
        MovementHistoryCard()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Fournisseurs
        SuppliersCard()
    }
}

@Composable
private fun ProductInfoCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Référence: ${product.reference}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Badge(
                    containerColor = if (product.currentStock <= product.minStock) 
                        MaterialTheme.colorScheme.errorContainer 
                    else MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = if (product.currentStock <= product.minStock) "Stock faible" else "En stock",
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            
            Divider()
            
            // Grille d'informations
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                InfoItem(
                    label = "Prix unitaire",
                    value = product.price,
                    icon = Icons.Default.Euro
                )
                InfoItem(
                    label = "Stock actuel",
                    value = "${product.currentStock} unités",
                    icon = Icons.Default.Inventory
                )
                InfoItem(
                    label = "Stock minimum",
                    value = "${product.minStock} unités",
                    icon = Icons.Default.Warning
                )
            }
        }
    }
}

@Composable
private fun InfoItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun StockStatsCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Statistiques de stock",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            // Barre de progression du stock
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Niveau de stock",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${product.currentStock}/${product.minStock * 3}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                LinearProgressIndicator(
                    progress = { (product.currentStock.toFloat() / (product.minStock * 3).toFloat()).coerceAtMost(1f) },
                    modifier = Modifier.fillMaxWidth(),
                    color = when {
                        product.currentStock <= product.minStock -> MaterialTheme.colorScheme.error
                        product.currentStock <= product.minStock * 2 -> MaterialTheme.colorScheme.secondary
                        else -> MaterialTheme.colorScheme.primary
                    }
                )
            }
            
            // Métriques supplémentaires
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MetricItem(
                    label = "Valeur totale",
                    value = "€${String.format("%.2f", product.currentStock * product.price.removePrefix("€").toDoubleOrNull()!! )}",
                    color = MaterialTheme.colorScheme.primary
                )
                MetricItem(
                    label = "Rotation",
                    value = "12/mois",
                    color = MaterialTheme.colorScheme.secondary
                )
                MetricItem(
                    label = "Dernière entrée",
                    value = "Il y a 3j",
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
private fun MetricItem(
    label: String,
    value: String,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun QuickActionsCard(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Actions rapides",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { /* Ajouter stock */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Ajouter")
                }
                
                OutlinedButton(
                    onClick = { /* Retirer stock */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Retirer")
                }
                
                OutlinedButton(
                    onClick = { /* Commander */ },
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(Icons.Default.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Commander")
                }
            }
        }
    }
}

@Composable
private fun MovementHistoryCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Historique des mouvements",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { /* Voir tout */ }) {
                    Text("Voir tout")
                }
            }
            
            // Liste des derniers mouvements
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MovementItem(
                    type = "Entrée",
                    quantity = "+50",
                    date = "15/03/2024",
                    reason = "Réapprovisionnement"
                )
                MovementItem(
                    type = "Sortie",
                    quantity = "-15",
                    date = "14/03/2024",
                    reason = "Commande client #1234"
                )
                MovementItem(
                    type = "Entrée",
                    quantity = "+25",
                    date = "10/03/2024",
                    reason = "Retour client"
                )
            }
        }
    }
}

@Composable
private fun MovementItem(
    type: String,
    quantity: String,
    date: String,
    reason: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = if (quantity.startsWith("+")) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
            contentDescription = null,
            tint = if (quantity.startsWith("+")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = reason,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "$type • $date",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Text(
            text = quantity,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = if (quantity.startsWith("+")) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun SuppliersCard() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Fournisseurs",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SupplierItem(
                    name = "TechDistrib SA",
                    price = "€1,250.00",
                    deliveryTime = "3-5 jours",
                    isPrimary = true
                )
                SupplierItem(
                    name = "ElectroWorld",
                    price = "€1,299.00",
                    deliveryTime = "5-7 jours",
                    isPrimary = false
                )
            }
        }
    }
}

@Composable
private fun SupplierItem(
    name: String,
    price: String,
    deliveryTime: String,
    isPrimary: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Business,
            contentDescription = null,
            tint = if (isPrimary) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Column(modifier = Modifier.weight(1f)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                if (isPrimary) {
                    Badge(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "Principal",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
            Text(
                text = "Livraison: $deliveryTime",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Text(
            text = price,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}