package com.babe.fata

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffold
import androidx.compose.material3.adaptive.layout.SupportingPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberSupportingPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.babe.fata.ui.AppScreen

import com.babe.fata.ui.VoirPlusScreen
import com.babe.fata.ui.theme.AppTheme
import compose.icons.FontAwesomeIcons
import compose.icons.fontawesomeicons.Brands
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.ArrowLeft
import compose.icons.fontawesomeicons.solid.Envelope
import compose.icons.fontawesomeicons.solid.Eye
import compose.icons.fontawesomeicons.solid.EyeSlash
import compose.icons.fontawesomeicons.solid.LockOpen
import fata.composeapp.generated.resources.Res
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import io.github.alexzhirkevich.cupertino.icons.CupertinoIcons
import io.github.alexzhirkevich.cupertino.icons.outlined.Backward
import io.github.alexzhirkevich.cupertino.icons.outlined.AppleLogo
import io.github.alexzhirkevich.cupertino.icons.outlined.PersonCircle

import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
@Composable
fun App( ){

    var themeSelection by rememberSaveable { mutableStateOf("Light") }
    val darkTheme = when (themeSelection) {
        "Light" -> true
        "Dark" -> false
        else -> isSystemInDarkTheme()
    }
    AppTheme(darkTheme = darkTheme) {
        Surface(modifier = Modifier.fillMaxSize()) {
            MainNavHost(
                themeSelection = themeSelection,
                onThemeChange = { themeSelection = it }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun MainNavHost(
    themeSelection: String,
    onThemeChange: (String) -> Unit


) {
    val navController = rememberNavController()
    var currentUser = User(
        name = ""
    )



    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onStart = {
                    navController.navigate("welcome") {
                        popUpTo(navController.graph.findStartDestination()) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onSignIn = {
                    navController.navigate("AppScreen")
                }
            )
        }




        composable("AppScreen"){

             AppScreen(

                 onSignOutClicked = {},
                 currentUser = currentUser,

                 themeSelection =themeSelection,
                 onThemeChange = {
                     onThemeChange(it)
                 })


        }


    }
}

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun HomeScreen(
    onStart: () -> Unit,
    onSignIn: () -> Unit
) {
    val navigator = rememberSupportingPaneScaffoldNavigator()
    val scope = rememberCoroutineScope()


    SupportingPaneScaffold(

        value = navigator.scaffoldValue,
        directive = navigator.scaffoldDirective,
        mainPane = {
            AnimatedPane {


                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(32.dp)
                    ) {
                    // --- Remplacement de l’icône Apple par l’animation Lottie Netflix ---
                    SampleCompottieAnimation(
                        modifier = Modifier
                            .size(150.dp)   // taille équivalente à l’icône précédente
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "",
                        style = MaterialTheme.typography.headlineLarge,
                        fontSize = 32.sp
                    )
                    Spacer(modifier = Modifier.height(32.dp))
                    if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Supporting] == PaneAdaptedValue.Hidden) {



                            Button(
                                onClick = {
                                    navigator.navigateTo(SupportingPaneScaffoldRole.Supporting)
                                },
                                modifier = Modifier
                                    .padding(64.dp)
                                    .align(Alignment.CenterHorizontally)
                            ) {
                                Text(text = "Démarrer")
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            }
        },
        supportingPane = {
            AnimatedPane {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    if (navigator.scaffoldValue[SupportingPaneScaffoldRole.Main] == PaneAdaptedValue.Hidden) {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    navigator.navigateBack()
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Retour"
                            )
                        }
                    }

                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(32.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Bienvenue sur l'application",
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontSize =  36.sp, // Smaller on small screens
                                lineHeight =  48.sp
                            ),
                            fontWeight = FontWeight.W400
                        )

                        Divider(modifier = Modifier.width(40.dp))

                        Text(
                            text = "Explorez l’univers du cinéma et des séries !\n" +
                                    "Trouvez des films, des séries, des acteurs, et toutes les informations qu’il vous faut grâce à l’API TMDb. Suivez vos favoris, découvrez des recommandations personnalisées, et ne manquez aucune nouveauté du grand écran.",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.W400
                        )
                        Text(
                            text = "FataApp",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.W400
                        )

                        Button(onClick = onSignIn) {
                            Text(text = "Continuez")
                        }
                    }
                }
            }
        }
    )
}






@OptIn(ExperimentalResourceApi::class)
@Composable
fun SampleCompottieAnimation(
    modifier: Modifier = Modifier
) {
    // 1) On charge le JSON une seule fois dans un state
    val jsonString by produceState<String?>(initialValue = null) {
        // ce bloc s'exécute en coroutine
        value = Res.readBytes("drawable/animations/netflix.json")
            .decodeToString()
    }

    // 2) only when jsonString is non-null, on initialise la composition
    val composition by rememberLottieComposition(
        spec = jsonString
            ?.let { LottieCompositionSpec.JsonString(it) }
            ?: return  // si pas encore chargé, on ne dessine rien
    )

    // 3) on anime la composition en boucle infinie
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = Compottie.IterateForever
    )

    // 4) on affiche l’animation
    Image(
        painter = rememberLottiePainter(
            composition = composition,
            progress = { progress }
        ),
        contentDescription = "Animation Lottie",
        modifier = modifier.fillMaxSize()
    )
}
