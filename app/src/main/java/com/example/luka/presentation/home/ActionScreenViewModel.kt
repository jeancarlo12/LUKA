package com.example.luka.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.luka.data.repository.AuthRepositoryImpl

import com.example.luka.domain.repository.AuthRepository

class ActionViewModel : ViewModel() {
    private val repository: AuthRepository = AuthRepositoryImpl()

    var successMessage = mutableStateOf("")
    var errorMessage = mutableStateOf("")
    var receiver = mutableStateOf("")
    var amount = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    fun updateReceiver(value: String) {
        receiver.value = value
    }

    fun updateAmount(value: String) {
        amount.value = value
    }

    fun sendTransfer(onSuccess: () -> Unit) {
        if (isLoading.value) return

        errorMessage.value = ""
        successMessage.value = ""

        if (receiver.value.isEmpty()) {
            errorMessage.value = "Recipient required"
            return
        }

        val transferAmount = amount.value.toDoubleOrNull()
        if (transferAmount == null || transferAmount <= 0) {
            errorMessage.value = "Invalid amount"
            return
        }

        isLoading.value = true

        repository.transfer(recipientEmail = receiver.value, amount = transferAmount) { success, message ->
            isLoading.value = false
            if (success) {
                successMessage.value = message
                onSuccess()
            } else {
                errorMessage.value = message
            }
        }
    }
}