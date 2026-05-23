package com.example.luka.register

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.luka.data.AuthRepositoryImpl
import com.example.luka.domain.RegisterUseCase
import com.example.luka.domain.model.User
import kotlinx.coroutines.delay

class registerViewModel(
    private val registerUseCase: RegisterUseCase = RegisterUseCase(AuthRepositoryImpl())
) : ViewModel() {

    var fullName = mutableStateOf("")
    var Email = mutableStateOf("")
    val DocumentNumber = mutableStateOf("")
    var Password = mutableStateOf("")
    var ConfirmPassword = mutableStateOf("")

    val isLoading = mutableStateOf(false)

    fun register(
        fullName: String,
        Email: String,
        DocumentNumber: String,
        Password: String,
        ConfirmPassword: String,
        OnResult: (Boolean, Int) -> Unit
    ) {
        if (Password != ConfirmPassword) {
            OnResult(false, 1) // 1 could represent password mismatch
            return
        }

        isLoading.value = true
        Log.d("RegisterViewModel", "Register function called with email: $Email")

        val user = User(
            fullName = fullName,
            email = Email,
            documentNumber = DocumentNumber,
            password = Password
        )

        registerUseCase(user) { success, code ->
            Log.d("RegisterViewModel", "UseCase result: success=$success, code=$code")
            isLoading.value = false
            OnResult(success, code)
        }
    }

    suspend fun onRegisterSelected() {
        isLoading.value = true
        delay(4000)
        isLoading.value = false
    }

    companion object {
        fun onRegisterSelected() {
            TODO("Not yet implemented")
        }
    }
}
