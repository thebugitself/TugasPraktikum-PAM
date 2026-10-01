package com.akhdan.newsfeedsimulator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.Dispatchers

class MainActivity : ComponentActivity() {
    private lateinit var newsManager: NewsManager

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        
        // Inisialisasi NewsManager
        val repository = NewsRepository()
        newsManager = NewsManager(repository, Dispatchers.IO)
        
        // Memulai simulasi flow
        newsManager.startSimulation()

        setContent {
            MaterialTheme {
                NewsFeedScreen(newsManager)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Menghentikan simulasi saat Activity dihancurkan
        newsManager.stopSimulation()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsFeedScreen(newsManager: NewsManager) {
    val readCount by newsManager.readCount.collectAsStateWithLifecycle()
    val newsList by newsManager.newsList.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("News Feed Simulator") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Text(
                    text = "Total Berita Dibaca: $readCount",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(newsList) { news ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            text = news.formattedText,
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}