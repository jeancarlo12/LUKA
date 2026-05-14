package com.example.luka.register


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.delay

class registerViewModel: ViewModel(){

    fun (
        fullName: String

    )

    val _isLoading = mutableStateOf(true)

    suspend fun onRegisterSelected() {

        _isLoading.value = true
        delay(4000)

    }

    companion object {
        fun onRegisterSelected() {
            TODO("Not yet implemented")
        }
    }
}