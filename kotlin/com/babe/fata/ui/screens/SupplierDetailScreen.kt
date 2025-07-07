package com.babe.fata.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplierDetailScreen(
    supplier: Supplier,
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
                    text = "Détails du fournisseur",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Text(
                text = "Détails du fournisseur",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
        
        // Informations principales
        SupplierInfoCard(supplier = supplier)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Statistiques
        SupplierStatsCard(supplier = supplier)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Informations de contact détaillées
        ContactDetailsCard(supplier = supplier)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Produits fournis
        SuppliedProductsCard(supplier = supplier)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Historique des commandes
        OrderHistoryCard(supplier = supplier)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Actions
        SupplierActionsCard(supplier = supplier)
    }
}

@Composable
private fun SupplierInfoCard(supplier: Supplier) {
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
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = androidx.compose.foundation.shape.CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = supplier.name.take(2).uppercase(),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    
                    Column {
                        Text(
                            text = supplier.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = supplier.category,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Badge(
                    containerColor = when (supplier.status) {
                        "Actif" -> MaterialTheme.colorScheme.primaryContainer
                        "Inactif" -> MaterialTheme.colorScheme.surfaceVariant
                        "En attente" -> MaterialTheme.colorScheme.secondaryContainer
                        "Suspendu" -> MaterialTheme.colorScheme.errorContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Text(
                        text = supplier.status,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
            
            Divider()
            
            // Note et évaluation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Évaluation",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = if (index < supplier.rating.toInt()) Icons.Default.Star else Icons.Default.StarBorder,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Text(
                            text = "${supplier.rating}/5",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Partenaire depuis",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Janvier 2022",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun SupplierStatsCard(supplier: Supplier) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Statistiques",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatItem(
                    label = "Produits",
                    value = "${supplier.productCount}",
                    icon = Icons.Default.Inventory,
                    color = MaterialTheme.colorScheme.primary
                )
                StatItem(
                    label = "Commandes",
                    value = "${supplier.orderCount}",
                    icon = Icons.Default.ShoppingCart,
                    color = MaterialTheme.colorScheme.secondary
                )
                StatItem(
                    label = "Délai moyen",
                    value = "5 jours",
                    icon = Icons.Default.Schedule,
                    color = MaterialTheme.colorScheme.tertiary
                )
                StatItem(
                    label = "Fiabilité",
                    value = "98%",
                    icon = Icons.Default.CheckCircle,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun StatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: androidx.compose.ui.graphics.Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ContactDetailsCard(supplier: Supplier) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Informations de contact",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ContactDetailItem(
                    icon = Icons.Default.Email,
                    label = "Email",
                    value = supplier.email,
                    action = "Envoyer un email"
                )
                ContactDetailItem(
                    icon = Icons.Default.Phone,
                    label = "Téléphone",
                    value = supplier.phone,
                    action = "Appeler"
                )
                ContactDetailItem(
                    icon = Icons.Default.LocationOn,
                    label = "Adresse",
                    value = supplier.address,
                    action = "Voir sur la carte"
                )
                ContactDetailItem(
                    icon = Icons.Default.Language,
                    label = "Site web",
                    value = "www.${supplier.name.lowercase().replace(" ", "")}.com",
                    action = "Visiter"
                )
            }
        }
    }
}

@Composable
private fun ContactDetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String,
    action: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
        
        TextButton(onClick = { /* Action */ }) {
            Text(action)
        }
    }
}

@Composable
private fun SuppliedProductsCard(supplier: Supplier) {
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
                    text = "Produits fournis",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { /* Voir tous */ }) {
                    Text("Voir tous")
                }
            }
            
            LazyColumn(
                modifier = Modifier.height(200.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(getSupplierProducts(supplier)) { product ->
                    ProductItem(product = product)
                }
            }
        }
    }
}

@Composable
private fun ProductItem(product: SupplierProduct) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Surface(
            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(40.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Icon(
                    imageVector = Icons.Default.Inventory,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = "Réf: ${product.reference}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Text(
            text = product.price,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun OrderHistoryCard(supplier: Supplier) {
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
                    text = "Dernières commandes",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                TextButton(onClick = { /* Voir historique */ }) {
                    Text("Historique complet")
                }
            }
            
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OrderItem(
                    orderNumber = "CMD-2024-0156",
                    date = "15/03/2024",
                    amount = "€3,245.80",
                    status = "Livrée"
                )
                OrderItem(
                    orderNumber = "CMD-2024-0142",
                    date = "08/03/2024",
                    amount = "€1,890.50",
                    status = "En cours"
                )
                OrderItem(
                    orderNumber = "CMD-2024-0128",
                    date = "01/03/2024",
                    amount = "€2,156.30",
                    status = "Livrée"
                )
            }
        }
    }
}

@Composable
private fun OrderItem(
    orderNumber: String,
    date: String,
    amount: String,
    status: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = Icons.Default.Receipt,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = orderNumber,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = date,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = amount,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Badge(
                containerColor = when (status) {
                    "Livrée" -> MaterialTheme.colorScheme.primaryContainer
                    "En cours" -> MaterialTheme.colorScheme.secondaryContainer
                    "Annulée" -> MaterialTheme.colorScheme.errorContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                }
            ) {
                Text(
                    text = status,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Composable
private fun SupplierActionsCard(supplier: Supplier) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Actions",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { /* Nouvelle commande */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Nouvelle commande")
                }
                
                OutlinedButton(
                    onClick = { /* Modifier */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Modifier les informations")
                }
                
                OutlinedButton(
                    onClick = { /* Évaluer */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Star, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Évaluer le fournisseur")
                }
            }
        }
    }
}

// Fonctions utilitaires
@Composable
private fun getSupplierProducts(supplier: Supplier): List<SupplierProduct> {
    return listOf(
        SupplierProduct("Ordinateur portable Dell XPS", "DELL-XPS-001", "€1,299.99"),
        SupplierProduct("Écran 27 pouces 4K", "MON-4K-001", "€399.99"),
        SupplierProduct("Clavier mécanique RGB", "KEY-RGB-001", "€129.99"),
        SupplierProduct("Souris sans fil", "MOUSE-001", "€29.99"),
        SupplierProduct("Webcam HD 1080p", "CAM-HD-001", "€79.99")
    )
}

data class SupplierProduct(
    val name: String,
    val reference: String,
    val price: String
)