package com.babe.fata.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.currentWindowDpSize
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.babe.fata.ui.BoraStockDestination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationPane(
    currentDestination: BoraStockDestination,
    onDestinationSelected: (BoraStockDestination) -> Unit,
    windowSize: DpSize,
    modifier: Modifier = Modifier
) {
    val isCompact = windowSize.width < 600.dp
    
    if (isCompact) {
        // Navigation compacte pour petits écrans
        CompactNavigation(
            currentDestination = currentDestination,
            onDestinationSelected = onDestinationSelected,
            modifier = modifier
        )
    } else {
        // Navigation étendue pour écrans moyens et larges
        ExtendedNavigation(
            currentDestination = currentDestination,
            onDestinationSelected = onDestinationSelected,
            modifier = modifier
        )
    }
}

@Composable
private fun CompactNavigation(
    currentDestination: BoraStockDestination,
    onDestinationSelected: (BoraStockDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        modifier = modifier.fillMaxHeight(),
        header = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                Text(
                    text = "BS",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    ) {
        BoraStockDestination.entries.forEach { destination ->
            NavigationRailItem(
                icon = { Icon(destination.icon, contentDescription = destination.label) },
                label = { Text(destination.label) },
                selected = currentDestination == destination,
                onClick = { onDestinationSelected(destination) }
            )
        }
    }
}

@Composable
private fun ExtendedNavigation(
    currentDestination: BoraStockDestination,
    onDestinationSelected: (BoraStockDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationDrawer(
        modifier = modifier.fillMaxHeight().width(280.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // En-tête
            Column(
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                Text(
                    text = "BoraStock",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Gestion de stock",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Divider(modifier = Modifier.padding(bottom = 16.dp))
            
            // Navigation items
            BoraStockDestination.entries.forEach { destination ->
                NavigationDrawerItem(
                    icon = { Icon(destination.icon, contentDescription = null) },
                    label = { Text(destination.label) },
                    selected = currentDestination == destination,
                    onClick = { onDestinationSelected(destination) },
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Informations système
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Système",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Version: 1.0.0",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Dernière sync: Maintenant",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
private fun NavigationDrawer(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 1.dp
    ) {
        content()
    }
}