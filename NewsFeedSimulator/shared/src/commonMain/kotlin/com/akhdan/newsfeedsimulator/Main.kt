package com.akhdan.newsfeedsimulator

import kotlinx.coroutines.*

fun main() = runBlocking {
    println("=== Memulai News Feed Simulator ===")

    val repository = NewsRepository()
    val manager = NewsManager(repository)

    // Mulai aliran Flow
    manager.startSimulation()

    // Kita jalankan simulasi selama 15 detik, lalu hentikan otomatis
    delay(15000L)

    manager.stopSimulation()
    println("\n=== Simulasi Selesai ===")
    println("Skor Total Berita Dibaca: ${manager.readCount.value}")
}