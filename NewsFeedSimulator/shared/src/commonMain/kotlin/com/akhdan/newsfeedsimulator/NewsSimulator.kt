package com.akhdan.newsfeedsimulator
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.random.Random

data class News(
    val id: Int,
    val title: String,
    val category: String,
    val isBreakingNews: Boolean = false
)

open class NewsRepository {
    private val categories = listOf("Tech", "Sports", "Politics", "Cybersecurity")

    // Flow yang mensimulasikan data berita baru setiap 2 detik
    open fun getNewsStream(): Flow<News> = flow {
        var idCounter = 1

        while (true) {
            delay(2000) // Delay 2 detik

            // Trigger error acak (peluang 10%) untuk didemonstrasikan di blok .catch
            if (Random.nextInt(10) == 0) {
                throw Exception("Koneksi terputus saat mengambil berita!")
            }

            val news = News(
                id = idCounter,
                title = "Berita Utama $idCounter",
                category = categories.random(),
                isBreakingNews = Random.nextBoolean()
            )

            emit(news) // Memancarkan berita ke aliran
            idCounter++
        }
    }

    // Coroutines untuk mengambil detail berita secara async
    open suspend fun fetchNewsDetail(newsId: Int): String {
        delay(1500) // Simulasi network call yang memblokir
        return "Ini adalah konten detail dan analisis mendalam untuk berita ID $newsId."
    }
}
