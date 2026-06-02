package com.example.luka.presentation.profile

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luka.data.repository.AuthRepositoryImpl
import com.example.luka.domain.model.User
import com.example.luka.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository: AuthRepository = AuthRepositoryImpl()

    var user = mutableStateOf<User?>(null)
    var isLoading = mutableStateOf(false)
    var updateMessage = mutableStateOf("")

    fun loadUserData() {
        isLoading.value = true
        viewModelScope.launch {
            user.value = repository.getFullUserData()
            isLoading.value = false
        }
    }

    fun updatePhone(newPhone: String) {
        viewModelScope.launch {
            val success = repository.updatePhoneNumber(newPhone)
            if (success) {
                updateMessage.value = "Phone updated!"
                loadUserData()
            } else {
                updateMessage.value = "Error updating phone"
            }
        }
    }

    fun updateEmail(newEmail: String) {
        viewModelScope.launch {
            val success = repository.updateEmail(newEmail)
            if (success) {
                updateMessage.value = "Email updated!"
                loadUserData()
            } else {
                updateMessage.value = "Error updating email (Re-login required)"
            }
        }
    }

    fun updatePassword(newPass: String) {
        viewModelScope.launch {
            val success = repository.updatePassword(newPass)
            if (success) {
                updateMessage.value = "Password updated!"
            } else {
                updateMessage.value = "Error updating password (Re-login required)"
            }
        }
    }
}
