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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovementDetailScreen(
    movement: StockMovement,
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
                    text = "Détails du mouvement",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        } else {
            Text(
                text = "Détails du mouvement",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )
        }
        
        // Informations principales du mouvement
        MovementInfoCard(movement = movement)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Détails du produit concerné
        ProductInfoCard(movement = movement)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Informations de traçabilité
        TraceabilityCard(movement = movement)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Actions disponibles
        ActionsCard(movement = movement)
    }
}

@Composable
private fun MovementInfoCard(movement: StockMovement) {
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
                        text = movement.type,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    movement.reference?.let { ref ->
                        Text(
                            text = "Référence: $ref",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                
                Badge(
                    containerColor = when (movement.type) {
                        "Entrée" -> MaterialTheme.colorScheme.primaryContainer
                        "Sortie" -> MaterialTheme.colorScheme.errorContainer
                        "Transfert" -> MaterialTheme.colorScheme.secondaryContainer
                        else -> MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Text(
                        text = movement.type,
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            
            Divider()
            
            // Grille d'informations principales
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                MovementInfoItem(
                    label = "Quantité",
                    value = "${if (movement.quantity > 0) "+" else ""}${movement.quantity}",
                    icon = Icons.Default.Numbers,
                    valueColor = when (movement.type) {
                        "Entrée" -> MaterialTheme.colorScheme.primary
                        "Sortie" -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.onSurface
                    }
                )
                movement.value?.let { value ->
                    MovementInfoItem(
                        label = "Valeur",
                        value = value,
                        icon = Icons.Default.Euro,
                        valueColor = MaterialTheme.colorScheme.primary
                    )
                }
                MovementInfoItem(
                    label = "Date",
                    value = movement.timestamp,
                    icon = Icons.Default.Schedule,
                    valueColor = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}

@Composable
private fun MovementInfoItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    valueColor: androidx.compose.ui.graphics.Color = MaterialTheme.colorScheme.onSurface
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
            fontWeight = FontWeight.Bold,
            color = valueColor
        )
    }
}

@Composable
private fun ProductInfoCard(movement: StockMovement) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Produit concerné",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Surface(
                    shape = androidx.compose.foundation.shape.CircleShape,
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            imageVector = Icons.Default.Inventory,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = movement.productName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Stock actuel: 45 unités",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Emplacement: A-12-03",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                
                TextButton(onClick = { /* Voir produit */ }) {
                    Text("Voir détails")
                }
            }
        }
    }
}

@Composable
private fun TraceabilityCard(movement: StockMovement) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Traçabilité",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TraceabilityItem(
                    label = "Utilisateur",
                    value = "Marie Dubois",
                    icon = Icons.Default.Person
                )
                TraceabilityItem(
                    label = "Entrepôt",
                    value = "Entrepôt Principal - Zone A",
                    icon = Icons.Default.Warehouse
                )
                TraceabilityItem(
                    label = "Motif",
                    value = getMovementReason(movement),
                    icon = Icons.Default.Description
                )
                if (movement.type == "Entrée") {
                    TraceabilityItem(
                        label = "Fournisseur",
                        value = "TechDistrib SA",
                        icon = Icons.Default.Business
                    )
                    TraceabilityItem(
                        label = "Bon de livraison",
                        value = "BL-2024-0156",
                        icon = Icons.Default.Receipt
                    )
                }
                if (movement.type == "Sortie") {
                    TraceabilityItem(
                        label = "Client",
                        value = "Entreprise ABC",
                        icon = Icons.Default.Business
                    )
                    TraceabilityItem(
                        label = "Bon de sortie",
                        value = "BS-2024-0089",
                        icon = Icons.Default.Receipt
                    )
                }
            }
        }
    }
}

@Composable
private fun TraceabilityItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(20.dp)
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
    }
}

@Composable
private fun ActionsCard(movement: StockMovement) {
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
                OutlinedButton(
                    onClick = { /* Imprimer */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Print, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Imprimer le bon")
                }
                
                OutlinedButton(
                    onClick = { /* Dupliquer */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Dupliquer le mouvement")
                }
                
                if (movement.type != "Ajustement") {
                    OutlinedButton(
                        onClick = { /* Annuler */ },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.error
                        )
                    ) {
                        Icon(Icons.Default.Cancel, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Annuler le mouvement")
                    }
                }
            }
        }
    }
}

private fun getMovementReason(movement: StockMovement): String {
    return when (movement.type) {
        "Entrée" -> "Réapprovisionnement fournisseur"
        "Sortie" -> "Commande client"
        "Transfert" -> "Réorganisation d'entrepôt"
        "Ajustement" -> "Correction d'inventaire"
        else -> "Non spécifié"
    }
}