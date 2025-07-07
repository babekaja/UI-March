package org.babetech.borastock

import androidx.compose.ui.graphics.Color

// Netflix Brand Colors - Couleurs principales
val NetflixRed = Color(0xFFE50914)
val NetflixDarkGray = Color(0xFF221F1F)
val NetflixLightGray = Color(0xFF737373)
val NetflixWhite = Color(0xFFFFFFFF)
val NetflixBlack = Color(0xFF000000)

// Couleurs supplémentaires pour une meilleure UX
val NetflixDarkRed = Color(0xFFB20710)
val NetflixMediumGray = Color(0xFF404040)
val NetflixLightBackground = Color(0xFFF8F8F8)

// Light Theme Colors
val primaryLight = NetflixRed
val onPrimaryLight = NetflixWhite
val primaryContainerLight = NetflixRed.copy(alpha = 0.12f)
val onPrimaryContainerLight = NetflixDarkRed

val secondaryLight = NetflixLightGray
val onSecondaryLight = NetflixWhite
val secondaryContainerLight = NetflixLightGray.copy(alpha = 0.12f)
val onSecondaryContainerLight = NetflixDarkGray

val backgroundLight = NetflixLightBackground
val onBackgroundLight = NetflixDarkGray
val surfaceLight = NetflixWhite
val onSurfaceLight = NetflixDarkGray

val errorLight = NetflixRed
val onErrorLight = NetflixWhite

val outlineLight = NetflixMediumGray
val scrimLight = Color(0x80000000)

// Dark Theme Colors
val primaryDark = NetflixRed
val onPrimaryDark = NetflixWhite
val primaryContainerDark = NetflixRed.copy(alpha = 0.24f)
val onPrimaryContainerDark = NetflixRed

val secondaryDark = NetflixLightGray
val onSecondaryDark = NetflixWhite
val secondaryContainerDark = NetflixLightGray.copy(alpha = 0.24f)
val onSecondaryContainerDark = NetflixLightGray

val backgroundDark = NetflixBlack
val onBackgroundDark = NetflixWhite
val surfaceDark = NetflixDarkGray
val onSurfaceDark = NetflixWhite

val errorDark = NetflixRed
val onErrorDark = NetflixWhite

val outlineDark = NetflixLightGray.copy(alpha = 0.5f)
val scrimDark = Color(0x80000000)