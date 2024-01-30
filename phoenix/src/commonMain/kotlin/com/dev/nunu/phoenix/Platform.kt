package com.dev.nunu.phoenix

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform