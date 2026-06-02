package com.example.luka.domain.useCase

import com.example.luka.domain.repository.AuthRepository

class TransferUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(recipientEmail: String, amount: Double): Pair<Boolean, String> {
        return repository.transfer(recipientEmail, amount)
    }
}
