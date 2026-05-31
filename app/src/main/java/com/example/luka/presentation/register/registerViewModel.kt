package com.example.luka.presentation.register

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.luka.data.repository.AuthRepositoryImpl
import com.example.luka.domain.useCase.RegisterUseCase
import com.example.luka.domain.model.User

class registerViewModel(
    private val registerUseCase: RegisterUseCase = RegisterUseCase(AuthRepositoryImpl())
) : ViewModel() {

    var fullName = mutableStateOf("")
    var Email = mutableStateOf("")
    val DocumentNumber = mutableStateOf("")
    var Password = mutableStateOf("")
    var ConfirmPassword = mutableStateOf("")

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    fun register(onResult: (Boolean) -> Unit) {
        val fName = fullName.value.trim()
        val emailVal = Email.value.trim()
        val docNum = DocumentNumber.value.trim()
        val pass = Password.value
        val confirmPass = ConfirmPassword.value

        if (fName.isEmpty() || emailVal.isEmpty() || docNum.isEmpty() || pass.isEmpty()) {
            errorMessage.value = "All fields are required"
            return
        }

        if (pass != confirmPass) {
            errorMessage.value = "Passwords do not match"
            return
        }

        if (pass.length < 6) {
            errorMessage.value = "Password must be at least 6 characters"
            return
        }

        isLoading.value = true
        errorMessage.value = null
        Log.d("RegisterViewModel", "Registering: $emailVal")

        val user = User(
            fullName = fName,
            email = emailVal,
            documentNumber = docNum,
            password = pass
        )

        registerUseCase(user) { success, code ->
            Log.d("RegisterViewModel", "Result: success=$success, code=$code")
            isLoading.value = false
            if (success) {
                onResult(true)
            } else {
                errorMessage.value = when(code) {
                    1 -> "Passwords do not match"
                    2 -> "Error saving user data"
                    3 -> "Authentication failed (Email might already be in use)"
                    else -> "Unknown error occurred"
                }
                onResult(false)
            }
        }
    }
}
