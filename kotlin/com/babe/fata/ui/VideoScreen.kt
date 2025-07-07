package org.babetech.borastock

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil3.compose.AsyncImage
import com.babe.fata.utils.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Composable
fun MainNavHost2() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "VideoScreen") {
        composable("VideoScreen") {
            VideoScreen(navController = navController)
        }

        composable(
            route = "VoirPlusScreen/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStack ->
            val movieId = backStack.arguments?.getInt("movieId")!!
            val vm = object : KoinComponent {
                val mv: MoviesViewModel by inject()
                val viewModel get() = mv
            }.viewModel

            LaunchedEffect(movieId) {
                if (vm.movieDetail?.id != movieId) {
                    vm.loadMovieDetail(movieId)
                }
            }

            val detail by remember { derivedStateOf { vm.movieDetail } }
            val isLoading by remember { derivedStateOf { vm.isLoading } }

            when {
                isLoading -> {
                    LoadingIndicator(message = "Chargement des détails...")
                }
                detail == null -> {
                    ErrorMessage(
                        message = "Film non trouvé",
                        onRetry = { vm.loadMovieDetail(movieId) }
                    )
                }
                else -> {
                    AdaptedSupportingPaneScaffold(
                        movie = detail!!,
                        navController = navController
                    )
                }
            }
        }

        composable(
            route = "MovieCreditsScreen/{movieId}",
            arguments = listOf(navArgument("movieId") { type = NavType.IntType })
        ) { backStackEntry ->
            val movieId = backStackEntry.arguments?.getInt("movieId")!!
            val vm = object : KoinComponent {
                val mv: MoviesViewModel by inject()
                val viewModel get() = mv
            }.viewModel

            LaunchedEffect(movieId) {
                vm.loadMovieCredits(movieId)
            }

            val credits by remember { derivedStateOf { vm.movieCredits } }
            val isLoading by remember { derivedStateOf { vm.isLoading } }

            MovieNavGraph(
                credits = credits,
                isLoading = isLoading
            )
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun VideoScreen(
    navController: NavController
) {
    val vm = remember {
        object : KoinComponent {
            val mv: MoviesViewModel by inject()
            val viewModel get() = mv
        }
    }.viewModel

    var selectedMovie by remember { mutableStateOf<Movie?>(null) }
    var searchQuery by remember { mutableStateOf("") }
    var currentPage by remember { mutableStateOf(1) }

    val navigator = rememberSupportingPaneScaffoldNavigator()

    // Chargement initial
    LaunchedEffect(Unit) {
        vm.loadAll(currentPage)
    }

    // Gestion des erreurs
    val errorState by vm.errorState.collectAsState()
    
    LaunchedEffect(errorState) {
        errorState?.let { error ->
            // Afficher un snackbar ou autre notification d'erreur
            println("Erreur: $error")
        }
    }

    SupportingPaneScaffold(
        value = navigator.scaffoldValue,
        directive = navigator.scaffoldDirective,
        mainPane = {
            AnimatedPane {
                Column(Modifier.fillMaxSize()) {
                    // Barre de recherche améliorée
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = { query ->
                            currentPage = 1
                            vm.searchMovies(query, currentPage)
                        }
                    )

                    Box(Modifier.fillMaxSize()) {
                        when {
                            vm.isLoading -> {
                                LoadingIndicator()
                            }
                            errorState != null -> {
                                ErrorMessage(
                                    message = errorState!!,
                                    onRetry = { 
                                        vm.clearError()
                                        vm.loadAll(currentPage) 
                                    }
                                )
                            }
                            searchQuery.isNotBlank() -> {
                                SearchResultsSection(
                                    results = vm.searchResults,
                                    currentPage = currentPage,
                                    onMovieClick = { movie ->
                                        selectedMovie = movie
                                        navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                                    },
                                    onPageChange = { page ->
                                        currentPage = page
                                        vm.searchMovies(searchQuery, page)
                                    }
                                )
                            }
                            else -> {
                                MoviesHomeContent(
                                    viewModel = vm,
                                    onMovieClick = { movie ->
                                        selectedMovie = movie
                                        navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                                    }
                                )
                            }
                        }
                    }
                }
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
                    if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Main] == PaneAdaptedValue.Hidden) {
                        IconButton(onClick = { navigator.navigateBack() }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Retour"
                            )
                        }
                    }

                    selectedMovie?.let { movie ->
                        MovieDetailPane(movie = movie, navController = navController)
                    } ?: run {
                        Text(
                            text = "Sélectionnez un film pour voir les détails",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    )
}

@Composable
private fun MoviesHomeContent(
    viewModel: MoviesViewModel,
    onMovieClick: (Movie) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            MovieSection(
                title = "Populaires",
                movies = viewModel.popularMovies,
                onMovieClick = onMovieClick
            )
        }
        item {
            MovieSection(
                title = "Maintenant à l'affiche",
                movies = viewModel.nowPlayingMovies,
                onMovieClick = onMovieClick
            )
        }
        item {
            MovieSection(
                title = "À venir",
                movies = viewModel.upcomingMovies,
                onMovieClick = onMovieClick
            )
        }
        item {
            MovieSection(
                title = "Bien notées",
                movies = viewModel.topRatedMovies,
                onMovieClick = onMovieClick
            )
        }
    }
}

@Composable
private fun MovieSection(
    title: String,
    movies: List<Movie>,
    onMovieClick: (Movie) -> Unit
) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
        )
        
        if (movies.isEmpty()) {
            Text(
                text = "Aucun film disponible",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        } else {
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(movies) { movie ->
                    MovieCard(
                        movie = movie,
                        onClick = { onMovieClick(movie) }
                    )
                }
            }
        }
    }
}

@Composable
private fun SearchResultsSection(
    results: List<Movie>,
    currentPage: Int,
    onMovieClick: (Movie) -> Unit,
    onPageChange: (Int) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = "Résultats de recherche (${results.size})",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        
        if (results.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Aucun résultat trouvé",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(results) { movie ->
                    MovieCardCompact(
                        movie = movie,
                        onClick = { onMovieClick(movie) }
                    )
                }
            }
            
            // Pagination
            PaginationControls(
                currentPage = currentPage,
                hasResults = results.isNotEmpty(),
                onPageChange = onPageChange
            )
        }
    }
}

@Composable
private fun PaginationControls(
    currentPage: Int,
    hasResults: Boolean,
    onPageChange: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            enabled = currentPage > 1,
            onClick = { onPageChange(currentPage - 1) }
        ) {
            Text("Précédent")
        }

        Text(
            text = "Page $currentPage",
            style = MaterialTheme.typography.bodyMedium
        )

        TextButton(
            enabled = hasResults,
            onClick = { onPageChange(currentPage + 1) }
        ) {
            Text("Suivant")
        }
    }
}

@Composable
private fun MovieDetailPane(
    movie: Movie,
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        AsyncImage(
            model = com.babe.fata.ApiConfig.getPosterUrl(movie.posterPath),
            contentDescription = movie.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentScale = ContentScale.Crop
        )
        
        Text(
            text = movie.title,
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        
        Text(
            text = "Sortie : ${movie.getFormattedReleaseDate()}",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Text(
            text = movie.getFormattedRating(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
        
        Text(
            text = "Langue : ${movie.originalLanguage?.uppercase() ?: "Inconnue"}",
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = movie.getShortOverview(),
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = { navController.navigate("VoirPlusScreen/${movie.id}") },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("En savoir plus")
        }
    }
}