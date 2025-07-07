package org.babetech.borastock

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil3.compose.AsyncImage
import com.babe.fata.model.CastMember
import com.babe.fata.model.Movie
import com.babe.fata.ui.viewmodel.MoviesViewModel
import io.github.alexzhirkevich.cupertino.icons.CupertinoIcons
import io.github.alexzhirkevich.cupertino.icons.outlined.Film
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.getValue

@OptIn(ExperimentalMaterial3AdaptiveApi::class, ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AdaptedSupportingPaneScaffold(
    movie: Movie,
    navController: NavHostController,
    viewModel: MoviesViewModel = koinViewModel()
) {
    // Charger les films similaires une seule fois
    LaunchedEffect(movie.id) {
        viewModel.loadSimilar(movie.id)
    }

    val similarMovies = viewModel.similarMovies
    val navigator = rememberSupportingPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()

    SupportingPaneScaffold(
        value = navigator.scaffoldValue,
        directive = navigator.scaffoldDirective,
        mainPane = {
            AnimatedPane {
                Box(modifier = Modifier.fillMaxSize()) {
                    VoirPlusScreen(movie = movie, navController = navController)
                    if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden) {
                        Button(
                            onClick = { scope.launch { navigator.navigateTo(SupportingPaneScaffoldRole.Supporting) } },
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(24.dp)
                                .fillMaxWidth(0.5f)
                        ) {
                            Text("Suivant")
                        }
                    }
                }
            }
        },
        supportingPane = {
            AnimatedPane {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Films similaires",
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (similarMovies.isEmpty()) {
                        Text("Aucun film similaire trouvé.")
                    } else {
                        SimilarMoviesGrid(
                            movies = similarMovies,
                            modifier = Modifier.fillMaxSize(),
                            onMovieClick = { clicked ->
                                // Naviguer vers l'écran VideoDetail avec l'ID
                                navController.navigate("VoirPlusScreen/${clicked.id}")
                            }
                        )
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SimilarMoviesGrid(
    movies: List<Movie>,
    modifier: Modifier = Modifier,
    onMovieClick: (Movie) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        items(movies) { movie ->
            SimilarMovieItem(movie = movie, onClick = { onMovieClick(movie) })
        }
    }
}

@Composable
fun SimilarMovieItem(
    movie: Movie,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .width(120.dp)
            .clickable { onClick() }
    ) {
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w200${movie.posterPath}",
            contentDescription = movie.title,
            modifier = Modifier
                .height(180.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = movie.title,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2
        )
    }
}

@Composable
fun VoirPlusScreen(movie: Movie,navController: NavHostController) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        )
        AsyncImage(
            model = "https://image.tmdb.org/t/p/w500${movie.posterPath}",
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .size(500.dp),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        // Détails principaux
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Date de sortie:\n${movie.releaseDate}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Durée:\n${movie.runtime} min",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Langue originale:\n${movie.originalLanguage?.uppercase()}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Text(
                    text = "Note:\n${movie.voteAverage} (${movie.voteCount})",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = movie.tagline.orEmpty(),
            style = MaterialTheme.typography.titleSmall.copy(fontStyle = MaterialTheme.typography.titleSmall.fontStyle, fontWeight = FontWeight.Medium),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Text(
            text = movie.overview,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        // Sociétés de production
        if (!movie.productionCompanies.isNullOrEmpty()) {
            Text(
                text = "Production",
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.Start).padding(bottom = 8.dp)
            )
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            ) {
                items(movie.productionCompanies) { company ->
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(80.dp)
                    ) {
                        if (!company.logoPath.isNullOrEmpty()) {
                            AsyncImage(
                                model = "https://image.tmdb.org/t/p/w200${company.logoPath}",
                                contentDescription = company.name,
                                modifier = Modifier
                                    .size(width = 60.dp, height = 60.dp),
                                contentScale = ContentScale.Fit
                            )
                        } else {
                            Image(
                                painter = rememberVectorPainter(image = CupertinoIcons.Default.Film),
                                contentDescription = company.name,
                                modifier = Modifier.size(60.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = company.name,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                            maxLines = 2,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { navController.navigate("MovieCreditsScreen/${movie.id}") },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Text(text = "Voir les acteurs ", style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp))
        }
    }
}


