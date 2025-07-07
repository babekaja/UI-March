package com.babe.fata.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
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
fun SuppliersScreen(
    onSupplierClick: (Supplier) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSize = currentWindowDpSize()
    val isLargeScreen = windowSize.width > 1000.dp
    var searchQuery by remember { mutableStateOf("") }
    var selectedStatus by remember { mutableStateOf("Tous") }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // En-tête avec recherche
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Fournisseurs",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Rechercher...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.width(300.dp)
                )
                
                IconButton(onClick = { /* Filtres */ }) {
                    Icon(Icons.Default.FilterList, contentDescription = "Filtres")
                }
                
                IconButton(onClick = { /* Ajouter fournisseur */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Ajouter")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filtres par statut
        StatusFilter(
            selectedStatus = selectedStatus,
            onStatusSelected = { selectedStatus = it }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Liste des fournisseurs
        val suppliers = getFilteredSuppliers(searchQuery, selectedStatus)
        
        if (isLargeScreen) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 350.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(suppliers) { supplier ->
                    SupplierCard(
                        supplier = supplier,
                        onClick = { onSupplierClick(supplier) }
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(suppliers) { supplier ->
                    SupplierListItem(
                        supplier = supplier,
                        onClick = { onSupplierClick(supplier) }
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusFilter(
    selectedStatus: String,
    onStatusSelected: (String) -> Unit
) {
    val statuses = listOf("Tous", "Actif", "Inactif", "En attente", "Suspendu")
    
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(statuses) { status ->
            FilterChip(
                onClick = { onStatusSelected(status) },
                label = { Text(status) },
                selected = selectedStatus == status
            )
        }
    }
}

@Composable
private fun SupplierCard(
    supplier: Supplier,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
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
                        text = supplier.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = supplier.category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            
            Divider()
            
            // Informations de contact
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ContactInfo(
                    icon = Icons.Default.Email,
                    text = supplier.email
                )
                ContactInfo(
                    icon = Icons.Default.Phone,
                    text = supplier.phone
                )
                ContactInfo(
                    icon = Icons.Default.LocationOn,
                    text = supplier.address
                )
            }
            
            Divider()
            
            // Métriques
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricItem(
                    label = "Produits",
                    value = "${supplier.productCount}",
                    color = MaterialTheme.colorScheme.primary
                )
                MetricItem(
                    label = "Commandes",
                    value = "${supplier.orderCount}",
                    color = MaterialTheme.colorScheme.secondary
                )
                MetricItem(
                    label = "Note",
                    value = "${supplier.rating}/5",
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
        }
    }
}

@Composable
private fun ContactInfo(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SupplierListItem(
    supplier: Supplier,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Avatar du fournisseur
            Surface(
                shape = androidx.compose.foundation.shape.CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = supplier.name.take(2).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = supplier.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${supplier.category} • ${supplier.productCount} produits",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
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
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                Text(
                    text = "★ ${supplier.rating}/5",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.tertiary
                )
            }
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun getFilteredSuppliers(searchQuery: String, status: String): List<Supplier> {
    val allSuppliers = listOf(
        Supplier(
            id = 1,
            name = "TechDistrib SA",
            category = "Informatique",
            status = "Actif",
            email = "contact@techdistrib.fr",
            phone = "+33 1 23 45 67 89",
            address = "123 Rue de la Tech, 75001 Paris",
            productCount = 156,
            orderCount = 45,
            rating = 4.8f
        ),
        Supplier(
            id = 2,
            name = "ElectroWorld",
            category = "Électronique",
            status = "Actif",
            email = "info@electroworld.com",
            phone = "+33 1 98 76 54 32",
            address = "456 Avenue de l'Électronique, 69000 Lyon",
            productCount = 89,
            orderCount = 23,
            rating = 4.2f
        ),
        Supplier(
            id = 3,
            name = "MobilierPro",
            category = "Mobilier",
            status = "En attente",
            email = "commercial@mobilierpro.fr",
            phone = "+33 4 56 78 90 12",
            address = "789 Boulevard du Mobilier, 13000 Marseille",
            productCount = 67,
            orderCount = 12,
            rating = 4.5f
        ),
        Supplier(
            id = 4,
            name = "FournitureExpress",
            category = "Fournitures",
            status = "Actif",
            email = "service@fournitureexpress.com",
            phone = "+33 2 34 56 78 90",
            address = "321 Rue des Fournitures, 44000 Nantes",
            productCount = 234,
            orderCount = 78,
            rating = 4.6f
        ),
        Supplier(
            id = 5,
            name = "InnovTech Solutions",
            category = "Informatique",
            status = "Suspendu",
            email = "contact@innovtech.fr",
            phone = "+33 5 67 89 01 23",
            address = "654 Place de l'Innovation, 31000 Toulouse",
            productCount = 45,
            orderCount = 8,
            rating = 3.2f
        )
    )
    
    return allSuppliers.filter { supplier ->
        val matchesSearch = searchQuery.isBlank() || 
                supplier.name.contains(searchQuery, ignoreCase = true) ||
                supplier.category.contains(searchQuery, ignoreCase = true) ||
                supplier.email.contains(searchQuery, ignoreCase = true)
        
        val matchesStatus = status == "Tous" || supplier.status == status
        
        matchesSearch && matchesStatus
    }
}

data class Supplier(
    val id: Int,
    val name: String,
    val category: String,
    val status: String,
    val email: String,
    val phone: String,
    val address: String,
    val productCount: Int,
    val orderCount: Int,
    val rating: Float
)