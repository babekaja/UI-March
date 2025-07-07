package com.babe.fata.utils

import com.babe.fata.model.Movie
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char

/**
 * Extensions pour améliorer la lisibilité du code
 */

/**
 * Formate la date de sortie d'un film
 */
fun Movie.getFormattedReleaseDate(): String {
    return try {
        releaseDate?.let { dateString ->
            val date = LocalDate.parse(dateString)
            date.format(LocalDate.Format {
                dayOfMonth()
                char('/')
                monthNumber()
                char('/')
                year()
            })
        } ?: "Date inconnue"
    } catch (e: Exception) {
        releaseDate ?: "Date inconnue"
    }
}

/**
 * Retourne l'année de sortie
 */
fun Movie.getReleaseYear(): String {
    return releaseDate?.take(4) ?: "Année inconnue"
}

/**
 * Formate la note du film
 */
fun Movie.getFormattedRating(): String {
    return voteAverage?.let { "★ ${String.format("%.1f", it)}" } ?: "Non noté"
}

/**
 * Retourne la durée formatée
 */
fun Movie.getFormattedRuntime(): String {
    return runtime?.let { minutes ->
        val hours = minutes / 60
        val remainingMinutes = minutes % 60
        when {
            hours > 0 -> "${hours}h ${remainingMinutes}min"
            else -> "${minutes}min"
        }
    } ?: "Durée inconnue"
}

/**
 * Vérifie si le film a une image de poster
 */
fun Movie.hasPoster(): Boolean = !posterPath.isNullOrBlank()

/**
 * Vérifie si le film a une image de backdrop
 */
fun Movie.hasBackdrop(): Boolean = !backdropPath.isNullOrBlank()

/**
 * Retourne un résumé court du film
 */
fun Movie.getShortOverview(maxLength: Int = 150): String {
    return if (overview.length <= maxLength) {
        overview
    } else {
        overview.take(maxLength).trimEnd() + "..."
    }
}

/**
 * Vérifie si le film est récent (sorti dans les 2 dernières années)
 */
fun Movie.isRecent(): Boolean {
    return try {
        releaseDate?.let { dateString ->
            val releaseYear = dateString.take(4).toIntOrNull()
            val currentYear = kotlinx.datetime.Clock.System.now().toString().take(4).toInt()
            releaseYear != null && (currentYear - releaseYear) <= 2
        } ?: false
    } catch (e: Exception) {
        false
    }
}

/**
 * Retourne les genres sous forme de chaîne
 */
fun Movie.getGenresString(): String {
    return genres?.joinToString(", ") { it.name } ?: "Genres inconnus"
}