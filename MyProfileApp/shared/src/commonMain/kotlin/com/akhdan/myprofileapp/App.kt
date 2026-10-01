package com.akhdan.myprofileapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import myprofileapp.shared.generated.resources.Res
import myprofileapp.shared.generated.resources.profile_pic

@Composable
fun App() {
    MaterialTheme {
        MyProfileScreen()
    }
}

@Composable
fun MyProfileScreen() {
    Box(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader(
                name = "Akhdan Arif Prayoga",
                role = "Security Researcher & Developer"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProfileCard(
                bio = "Mahasiswa Teknik Informatika ITERA dengan fokus pada offensive security & red teaming."
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    InfoItem(icon = Icons.Default.Email, text = "akhdan@example.com")
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoItem(icon = Icons.Default.Phone, text = "+62 812-3456-7890")
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoItem(icon = Icons.Default.LocationOn, text = "Lampung, Indonesia")
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { /* Aksi ketika diklik */ },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Connect")
            }
        }
    }
}

@Composable
fun ProfileHeader(name: String, role: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(Res.drawable.profile_pic),
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = name,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = role,
            fontSize = 16.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun ProfileCard(bio: String) {
    // konfigurasi elevasi menggunakan CardDefaults
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "About Me",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = bio,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun InfoItem(icon: ImageVector, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Color.Gray,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = text)
    }
}