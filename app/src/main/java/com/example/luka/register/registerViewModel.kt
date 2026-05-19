package com.example.luka.register

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {

    var fullName: MutableState<String> = mutableStateOf("")
    private val _isLoading = mutableStateOf(false)
    val isLoading: MutableState<Boolean> = _isLoading

    fun onRegisterSelected() {
        viewModelScope.launch {
            _isLoading.value = true
            delay(2000)
            _isLoading.value = false
            // Lógica de registro aquí
        }
    }
}
