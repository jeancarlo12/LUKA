package com.example.luka.domain.useCase

import com.example.luka.domain.repository.AuthRepository

class TransferMoneyUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(
        receiverEmail: String,
        amount: Double,
        onResult: (Boolean, String) -> Unit
    ) {
        repository.transfer(
            recipientEmail = receiverEmail,
            amount = amount,
            onResult = onResult
        )
    }
}