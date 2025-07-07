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
import com.babe.fata.ui.components.Product

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onProductClick: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSize = currentWindowDpSize()
    val isLargeScreen = windowSize.width > 1000.dp
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Tous") }
    
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
                text = "Produits",
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
                
                IconButton(onClick = { /* Ajouter produit */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Ajouter")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filtres par catégorie
        CategoryFilter(
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Liste des produits
        val products = getFilteredProducts(searchQuery, selectedCategory)
        
        if (isLargeScreen) {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(minSize = 300.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onClick = { onProductClick(product) }
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(products) { product ->
                    ProductListItem(
                        product = product,
                        onClick = { onProductClick(product) }
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryFilter(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit
) {
    val categories = listOf("Tous", "Informatique", "Mobilier", "Fournitures", "Électronique")
    
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                onClick = { onCategorySelected(category) },
                label = { Text(category) },
                selected = selectedCategory == category
            )
        }
    }
}

@Composable
private fun ProductCard(
    product: Product,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Réf: ${product.reference}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                Badge(
                    containerColor = if (product.currentStock <= product.minStock) 
                        MaterialTheme.colorScheme.errorContainer 
                    else MaterialTheme.colorScheme.primaryContainer
                ) {
                    Text(
                        text = if (product.currentStock <= product.minStock) "Rupture" else "En stock",
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
            
            Divider()
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Stock actuel",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${product.currentStock} unités",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
                
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Prix unitaire",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = product.price,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
            
            LinearProgressIndicator(
                progress = { (product.currentStock.toFloat() / (product.minStock * 2).toFloat()).coerceAtMost(1f) },
                modifier = Modifier.fillMaxWidth(),
                color = if (product.currentStock <= product.minStock) 
                    MaterialTheme.colorScheme.error 
                else MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
private fun ProductListItem(
    product: Product,
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
            // Indicateur de stock
            Box(
                modifier = Modifier.size(12.dp),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.foundation.Canvas(
                    modifier = Modifier.size(12.dp)
                ) {
                    drawCircle(
                        color = if (product.currentStock <= product.minStock) 
                            androidx.compose.ui.graphics.Color.Red 
                        else androidx.compose.ui.graphics.Color.Green
                    )
                }
            }
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Réf: ${product.reference} • Stock: ${product.currentStock}",
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
            
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun getFilteredProducts(searchQuery: String, category: String): List<Product> {
    val allProducts = listOf(
        Product(1, "Ordinateur portable Dell XPS 13", "DELL-XPS-001", 15, 10, "€1,299.99"),
        Product(2, "Souris sans fil Logitech", "LOG-MOUSE-001", 45, 20, "€29.99"),
        Product(3, "Clavier mécanique RGB", "KEY-RGB-001", 8, 15, "€129.99"),
        Product(4, "Écran 27 pouces 4K", "MON-4K-001", 3, 5, "€399.99"),
        Product(5, "Chaise de bureau ergonomique", "CHAIR-ERG-001", 12, 8, "€249.99"),
        Product(6, "Bureau réglable en hauteur", "DESK-ADJ-001", 2, 3, "€599.99"),
        Product(7, "Imprimante laser couleur", "PRINT-COL-001", 6, 4, "€189.99"),
        Product(8, "Webcam HD 1080p", "CAM-HD-001", 25, 15, "€79.99"),
        Product(9, "Casque audio Bluetooth", "HEAD-BT-001", 18, 12, "€159.99"),
        Product(10, "Tablette graphique", "TAB-GRAPH-001", 7, 5, "€89.99")
    )
    
    return allProducts.filter { product ->
        val matchesSearch = searchQuery.isBlank() || 
                product.name.contains(searchQuery, ignoreCase = true) ||
                product.reference.contains(searchQuery, ignoreCase = true)
        
        val matchesCategory = category == "Tous" || 
                when (category) {
                    "Informatique" -> product.name.contains("Ordinateur") || 
                            product.name.contains("Souris") || 
                            product.name.contains("Clavier") ||
                            product.name.contains("Écran") ||
                            product.name.contains("Webcam") ||
                            product.name.contains("Tablette")
                    "Mobilier" -> product.name.contains("Chaise") || product.name.contains("Bureau")
                    "Fournitures" -> product.name.contains("Imprimante")
                    "Électronique" -> product.name.contains("Casque")
                    else -> true
                }
        
        matchesSearch && matchesCategory
    }
}