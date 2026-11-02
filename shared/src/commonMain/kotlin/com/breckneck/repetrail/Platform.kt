package com.breckneck.repetrail

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform