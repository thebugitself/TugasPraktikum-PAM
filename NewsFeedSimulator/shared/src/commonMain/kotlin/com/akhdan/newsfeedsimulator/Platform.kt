package com.akhdan.newsfeedsimulator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform