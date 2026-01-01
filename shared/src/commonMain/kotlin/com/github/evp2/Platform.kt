package com.github.evp2

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform