package org.babetech.borastock

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage

import com.babe.fata.model.CastMember
import com.babe.fata.ui.viewmodel.MoviesViewModel
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MovieNavGraph(
    navController: NavController = rememberNavController(),
    credits: List<CastMember>,
    isLoading: Boolean,
    viewModel: MoviesViewModel = koinViewModel()
) {
    val navigator = rememberSupportingPaneScaffoldNavigator()
    val castDetail = viewModel.castDetails
    SupportingPaneScaffold(
        value = navigator.scaffoldValue,
        directive = navigator.scaffoldDirective,
        mainPane = {
            AnimatedPane {
                MovieCreditsScreen(
                    credits = credits,
                    isLoading = isLoading,
                    onCastClick = { cast ->
                        viewModel.loadCastDetails(cast.id)
                        navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                    }
                )
            }
        },
        supportingPane = {
            AnimatedPane {
                val scrollState = rememberScrollState()
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(16.dp)
                ) {
                    // Back button when main pane is hidden
                    if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Main] == PaneAdaptedValue.Hidden) {
                        IconButton(onClick = { navigator.navigateBack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Retour"
                            )
                        }
                    }

                    // Loading or detail view
                    if (castDetail == null) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {

                            CircularProgressIndicator()
                        }
                    } else {
                        CastDetailScreen(cast = castDetail!!)
                    }
                }
            }
        }
    )
}

@Composable
fun MovieCreditsScreen(
    credits: List<CastMember>,
    isLoading: Boolean,
    onCastClick: (CastMember) -> Unit
) {
    if (isLoading) {
        Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(credits) { cast ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCastClick(cast) }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = "https://image.tmdb.org/t/p/w200${cast.profile_path}",
                        contentDescription = cast.name,
                        modifier = Modifier.size(width = 80.dp, height = 120.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(text = cast.name, style = MaterialTheme.typography.bodyLarge)
                        Text(text = cast.character, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}

@Composable
fun CastDetailScreen(cast: CastDetails) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${cast.profile_path}",
            contentDescription = cast.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = cast.name, style = MaterialTheme.typography.headlineMedium)
        cast.birthday?.let {
            Text(text = "Né le : $it", style = MaterialTheme.typography.bodyMedium)
        }
        cast.place_of_birth?.let {
            Text(text = "Lieu : $it", style = MaterialTheme.typography.bodyMedium)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = cast.biography.takeIf { it.isNotBlank() } ?: "Pas de biographie disponible.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}
