package com.akhdan.myprofileapp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform