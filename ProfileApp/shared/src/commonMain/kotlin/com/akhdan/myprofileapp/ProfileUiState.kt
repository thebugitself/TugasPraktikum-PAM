package com.akhdan.myprofileapp

/**
 * UI State representing the profile screen data and configuration.
 */
data class ProfileUiState(
    val name: String = "Akhdan Arif Prayoga",
    val role: String = "Security Researcher & Developer",
    val bio: String = "Mahasiswa Teknik Informatika ITERA dengan fokus pada offensive security & red teaming.",
    val email: String = "akhdan@example.com",
    val phone: String = "+62 812-3456-7890",
    val location: String = "Lampung, Indonesia",
    val isDarkMode: Boolean = false,
    val isEditing: Boolean = false
)
