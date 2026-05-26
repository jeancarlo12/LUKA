package com.example.luka.domain.useCase

import com.example.luka.domain.repository.AuthRepository

class TransferUseCase(private val repository: AuthRepository) {
    operator fun invoke(recipientEmail: String, amount: Double, onResult: (Boolean, String) -> Unit) {
        repository.transfer(recipientEmail, amount, onResult)
    }
}