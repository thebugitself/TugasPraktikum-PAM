package com.akhdan.newsfeedsimulator

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class NewsManagerTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun testNewsManagerFlowAndState() = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        
        // Mock repository untuk emisi Flow deterministik
        val fakeRepository = object : NewsRepository() {
            override fun getNewsStream(): Flow<News> = flow {
                emit(News(1, "Berita 1", "Tech", false))
                emit(News(2, "Berita 2", "Sports", false)) // Tidak lolos filter
                emit(News(3, "Berita 3", "Cybersecurity", true))
            }

            override suspend fun fetchNewsDetail(newsId: Int): String {
                return "Fake Detail $newsId"
            }
        }
        
        val newsManager = NewsManager(fakeRepository, testDispatcher)
        
        // Initial state
        assertEquals(0, newsManager.readCount.value)
        assertEquals(0, newsManager.newsList.value.size)
        
        // Mulai simulasi
        newsManager.startSimulation()
        
        // Karena menggunakan testDispatcher, kita jalankan sisa task di queue
        advanceUntilIdle() 
        // pastikan semua coroutine sudah berjalan
        
        // Yang lolos filter adalah Tech (Berita 1) dan Cybersecurity (Berita 3)
        assertEquals(2, newsManager.readCount.value)
        assertEquals(2, newsManager.newsList.value.size)
        
        val firstNews = newsManager.newsList.value[0]
        assertEquals(1, firstNews.newsId)
        assertEquals("[📰 REGULER] Tech - Berita 1", firstNews.formattedText)
        
        val secondNews = newsManager.newsList.value[1]
        assertEquals(3, secondNews.newsId)
        assertEquals("[🚨 BREAKING] Cybersecurity - Berita 3", secondNews.formattedText)
        
        newsManager.stopSimulation()
    }
}
