package com.akhdan.myprofileapp

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel managing the Profile state (MVVM architecture).
 */
class ProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun toggleDarkMode() {
        _uiState.update { currentState ->
            currentState.copy(isDarkMode = !currentState.isDarkMode)
        }
    }

    fun setEditing(isEditing: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(isEditing = isEditing)
        }
    }

    fun updateProfile(
        name: String,
        role: String,
        bio: String,
        email: String,
        phone: String,
        location: String
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                name = name,
                role = role,
                bio = bio,
                email = email,
                phone = phone,
                location = location,
                isEditing = false
            )
        }
    }
}
