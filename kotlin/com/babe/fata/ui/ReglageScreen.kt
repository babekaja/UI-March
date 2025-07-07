package org.babetech.borastock

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.unit.dp
import fata.composeapp.generated.resources.Res
import io.github.alexzhirkevich.compottie.Compottie
import io.github.alexzhirkevich.compottie.LottieCompositionSpec
import io.github.alexzhirkevich.compottie.animateLottieCompositionAsState
import io.github.alexzhirkevich.compottie.rememberLottieComposition
import io.github.alexzhirkevich.compottie.rememberLottiePainter
import org.jetbrains.compose.resources.ExperimentalResourceApi

/**
 * Screen for changing app theme.
 * @param currentUser the logged in user (could be null)
 * @param initialTheme one of "Light", "Dark", "System"
 * @param onThemeSelected callback when user picks a theme
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReglageScreen(
    currentUser: User?,
    themeSelection: String,
    onThemeChange: (String) -> Unit
) {
    var selectedTheme by remember { mutableStateOf(themeSelection) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Paramètres  ") }
            )
        }
    ) { innerPadding ->

        SampleCompottieAnimation2()



        }
    }





@OptIn(ExperimentalResourceApi::class)
@Composable
fun SampleCompottieAnimation2(
    modifier: Modifier = Modifier
) {
    // 1) On charge le JSON une seule fois dans un state
    val jsonString by produceState<String?>(initialValue = null) {
        // ce bloc s'exécute en coroutine
        value = Res.readBytes("drawable/animations/setting.json")
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
