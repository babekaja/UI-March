package com.babe.fata.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
fun MovementsScreen(
    onMovementClick: (StockMovement) -> Unit,
    modifier: Modifier = Modifier
) {
    val windowSize = currentWindowDpSize()
    var selectedFilter by remember { mutableStateOf("Tous") }
    var selectedPeriod by remember { mutableStateOf("Cette semaine") }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // En-tête
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Mouvements de stock",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = { /* Exporter */ }) {
                    Icon(Icons.Default.FileDownload, contentDescription = "Exporter")
                }
                IconButton(onClick = { /* Nouveau mouvement */ }) {
                    Icon(Icons.Default.Add, contentDescription = "Nouveau")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filtres
        MovementFilters(
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it },
            selectedPeriod = selectedPeriod,
            onPeriodSelected = { selectedPeriod = it }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Statistiques rapides
        MovementStats()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Liste des mouvements
        val movements = getFilteredMovements(selectedFilter, selectedPeriod)
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movements) { movement ->
                MovementCard(
                    movement = movement,
                    onClick = { onMovementClick(movement) }
                )
            }
        }
    }
}

@Composable
private fun MovementFilters(
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    selectedPeriod: String,
    onPeriodSelected: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Filtres par type
        Text(
            text = "Type de mouvement",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf("Tous", "Entrées", "Sorties", "Transferts", "Ajustements")) { filter ->
                FilterChip(
                    onClick = { onFilterSelected(filter) },
                    label = { Text(filter) },
                    selected = selectedFilter == filter
                )
            }
        }
        
        // Filtres par période
        Text(
            text = "Période",
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Medium
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(listOf("Aujourd'hui", "Cette semaine", "Ce mois", "Personnalisé")) { period ->
                FilterChip(
                    onClick = { onPeriodSelected(period) },
                    label = { Text(period) },
                    selected = selectedPeriod == period
                )
            }
        }
    }
}

@Composable
private fun MovementStats() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem(
                label = "Entrées",
                value = "156",
                icon = Icons.Default.TrendingUp,
                color = MaterialTheme.colorScheme.primary
            )
            StatItem(
                label = "Sorties",
                value = "89",
                icon = Icons.Default.TrendingDown,
                color = MaterialTheme.colorScheme.error
            )
            StatItem(
                label = "Valeur totale",
                value = "€12,450",
                icon = Icons.Default.Euro,
                color = MaterialTheme.colorScheme.secondary
            )
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
private fun MovementCard(
    movement: StockMovement,
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
            // Icône du type de mouvement
            Surface(
                shape = androidx.compose.foundation.shape.CircleShape,
                color = when (movement.type) {
                    "Entrée" -> MaterialTheme.colorScheme.primaryContainer
                    "Sortie" -> MaterialTheme.colorScheme.errorContainer
                    "Transfert" -> MaterialTheme.colorScheme.secondaryContainer
                    else -> MaterialTheme.colorScheme.surfaceVariant
                },
                modifier = Modifier.size(48.dp)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Icon(
                        imageVector = when (movement.type) {
                            "Entrée" -> Icons.Default.Add
                            "Sortie" -> Icons.Default.Remove
                            "Transfert" -> Icons.Default.SwapHoriz
                            else -> Icons.Default.Edit
                        },
                        contentDescription = null,
                        tint = when (movement.type) {
                            "Entrée" -> MaterialTheme.colorScheme.primary
                            "Sortie" -> MaterialTheme.colorScheme.error
                            "Transfert" -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                }
            }
            
            // Informations du mouvement
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = movement.productName,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "${movement.type} • ${movement.timestamp}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (movement.reference != null) {
                    Text(
                        text = "Réf: ${movement.reference}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Quantité et valeur
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${if (movement.quantity > 0) "+" else ""}${movement.quantity}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = when (movement.type) {
                        "Entrée" -> MaterialTheme.colorScheme.primary
                        "Sortie" -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
                if (movement.value != null) {
                    Text(
                        text = movement.value,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
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
private fun getFilteredMovements(filter: String, period: String): List<StockMovement> {
    val allMovements = listOf(
        StockMovement(1, "Entrée", "Ordinateur portable Dell", 25, "Aujourd'hui 14:30", "CMD-001", "€32,499.75"),
        StockMovement(2, "Sortie", "Souris sans fil", -15, "Aujourd'hui 12:15", "VTE-045", "€449.85"),
        StockMovement(3, "Entrée", "Clavier mécanique", 30, "Hier 16:45", "CMD-002", "€3,899.70"),
        StockMovement(4, "Transfert", "Écran 27 pouces", 5, "Hier 14:20", "TRF-012", null),
        StockMovement(5, "Sortie", "Chaise ergonomique", -3, "15/03/2024", "VTE-046", "€749.97"),
        StockMovement(6, "Ajustement", "Webcam HD", 2, "15/03/2024", "ADJ-008", null),
        StockMovement(7, "Entrée", "Casque Bluetooth", 40, "14/03/2024", "CMD-003", "€6,399.60"),
        StockMovement(8, "Sortie", "Tablette graphique", -8, "14/03/2024", "VTE-047", "€719.92")
    )
    
    return allMovements.filter { movement ->
        val matchesFilter = filter == "Tous" || 
                when (filter) {
                    "Entrées" -> movement.type == "Entrée"
                    "Sorties" -> movement.type == "Sortie"
                    "Transferts" -> movement.type == "Transfert"
                    "Ajustements" -> movement.type == "Ajustement"
                    else -> true
                }
        
        // Pour la démo, on filtre simplement par type
        matchesFilter
    }
}

// Extension de la data class StockMovement
data class StockMovement(
    val id: Int,
    val type: String,
    val productName: String,
    val quantity: Int,
    val timestamp: String,
    val reference: String? = null,
    val value: String? = null
)