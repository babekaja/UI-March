package org.babetech.borastock

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform