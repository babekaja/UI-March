package org.babetech.borastock

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.babe.fata.SampleCompottieAnimation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScreen(

    onSignOutClicked: () -> Unit,
    themeSelection: String,
    onThemeChange: (String) -> Unit
) {
    var appTheme by rememberSaveable { mutableStateOf("System") }
    var isMenuExpanded by rememberSaveable { mutableStateOf(false) }
    var currentDestination by rememberSaveable { mutableStateOf(AppDestinations.COURSES) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("FataAPP  ${themeSelection}", fontWeight = FontWeight.W900) },
                navigationIcon = {
                    SampleCompottieAnimation(modifier = Modifier.size(80.dp))
                },
                actions = {
                    IconButton(onClick = { isMenuExpanded = true }) {
                        AsyncImage(
                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                .data("https://lh3.googleusercontent.com/-Mr0HyPhd6MM/AAAAAAAAAAI/AAAAAAAAAAA/ALKGfkl-se6qziRp-4bZd00E00dw11cifQ/photo.jpg?sz=46")
                                .crossfade(true)
                                .build(),
                            contentDescription = "Profil",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                        DropdownMenu(
                            expanded = isMenuExpanded,
                            onDismissRequest = { isMenuExpanded = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Déconnexion") },
                                onClick = {
                                    isMenuExpanded = false
                                    onSignOutClicked()
                                }
                            )
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            NavigationSuiteScaffold(
                navigationSuiteItems = {
                    AppDestinations.entries.forEach { dest ->
                        item(
                            icon = { Icon(dest.icon, dest.contentDescription) },
                            label = { Text(dest.label) },
                            selected = dest == currentDestination,
                            onClick = { currentDestination = dest }
                        )
                    }
                }
            ) {
                when (currentDestination) {
                    AppDestinations.COURSES -> MainNavHost2()
                    AppDestinations.PROFILE -> ReglageScreen(
                        currentUser = currentUser,
                        themeSelection = appTheme,
                        onThemeChange = { theme -> appTheme = theme }
                    )
                }
            }
        }
    }
}
