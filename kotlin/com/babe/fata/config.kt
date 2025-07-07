package com.babe.fata

object ApiConfig {
    const val BASE_URL = "https://api.themoviedb.org/3"
    const val API_KEY = "622db0575653b61772697f61228e3d01"
    const val VIDEO_BASE_URL = "https://vidsrc.to/embed/movie/"
    const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"
    const val LANGUAGE = "fr-FR"
    
    // Tailles d'images disponibles
    object ImageSizes {
        const val POSTER_SMALL = "w200"
        const val POSTER_MEDIUM = "w300"
        const val POSTER_LARGE = "w500"
        const val BACKDROP_SMALL = "w300"
        const val BACKDROP_MEDIUM = "w780"
        const val BACKDROP_LARGE = "w1280"
        const val PROFILE_SMALL = "w185"
        const val PROFILE_MEDIUM = "w342"
    }
    
    // URLs complètes pour les images
    fun getPosterUrl(path: String?, size: String = ImageSizes.POSTER_MEDIUM): String {
        return if (path != null) "${IMAGE_BASE_URL}${size}${path}" else ""
    }
    
    fun getBackdropUrl(path: String?, size: String = ImageSizes.BACKDROP_MEDIUM): String {
        return if (path != null) "${IMAGE_BASE_URL}${size}${path}" else ""
    }
    
    fun getProfileUrl(path: String?, size: String = ImageSizes.PROFILE_MEDIUM): String {
        return if (path != null) "${IMAGE_BASE_URL}${size}${path}" else ""
    }
}