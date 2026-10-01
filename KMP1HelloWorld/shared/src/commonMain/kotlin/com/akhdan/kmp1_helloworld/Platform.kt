package com.akhdan.kmp1_helloworld

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform