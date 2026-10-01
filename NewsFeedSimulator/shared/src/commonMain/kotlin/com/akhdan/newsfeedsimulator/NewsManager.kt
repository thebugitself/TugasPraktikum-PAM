package com.akhdan.newsfeedsimulator

import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class NewsManager(
    private val repository: NewsRepository,
    dispatcher: CoroutineDispatcher = Dispatchers.Default
) {

    // StateFlow untuk menyimpan jumlah berita yang sudah dibaca
    private val _readCount = MutableStateFlow(0)
    val readCount: StateFlow<Int> = _readCount.asStateFlow()

    // StateFlow untuk menyimpan daftar berita yang akan ditampilkan di UI
    private val _newsList = MutableStateFlow<List<NewsDisplayData>>(emptyList())
    val newsList: StateFlow<List<NewsDisplayData>> = _newsList.asStateFlow()

    // Menggunakan scope dengan dispatcher yang diinjeksi untuk operasi latar belakang
    private val managerScope = CoroutineScope(dispatcher + SupervisorJob())

    fun startSimulation() {
        managerScope.launch {
            repository.getNewsStream()
                // BONUS: Implementasi error handling dengan .catch
                // Menangkap exception dari repository agar aplikasi tidak crash
                .catch { e ->
                    println("\n[ERROR TERDETEKSI] Waduh, jaringan putus: ${e.message}")
                    // Mengirim data darurat sebagai fallback pengganti error
                    emit(News(-1, "Sistem Dalam Perbaikan", "System", true))
                }
                // Filter berita berdasarkan kategori tertentu[cite: 1]
                // Hanya loloskan berita kategori Tech atau Cybersecurity
                .filter { news ->
                    news.category == "Tech" || news.category == "Cybersecurity" || news.category == "System"
                }
                // Syarat 3: Transform data menjadi format string yang bagus untuk UI[cite: 1]
                .map { news ->
                    val status = if (news.isBreakingNews) "🚨 BREAKING" else "📰 REGULER"
                    NewsDisplayData(news.id, "[$status] ${news.category} - ${news.title}")
                }
                // .collect memicu Flow agar mulai berjalan[cite: 1]
                .collect { displayData ->
                    println("\n>> Menerima Berita: ${displayData.formattedText}")

                    // Syarat 5: Coroutines (async/await) untuk ambil detail berita secara paralel/async[cite: 1]
                    coroutineScope {
                        val detailDeferred = async { repository.fetchNewsDetail(displayData.newsId) }

                        // .await() menunggu hasil dari simulasi network[cite: 1]
                        val detail = detailDeferred.await()
                        println("   Detail: $detail")
                    }

                    // Update StateFlow (increment dan penambahan ke list)
                    _readCount.value++
                    _newsList.value = _newsList.value + displayData
                    println("   [Total Berita Terbaca: ${readCount.value}]")
                }
        }
    }

    fun stopSimulation() {
        managerScope.cancel() // Membersihkan coroutines yang tidak dibutuhkan[cite: 1]
    }
}

// Data class bantuan untuk hasil map
data class NewsDisplayData(val newsId: Int, val formattedText: String)