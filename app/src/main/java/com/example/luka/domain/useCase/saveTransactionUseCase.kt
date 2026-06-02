package com.example.luka.domain.useCase

import com.example.luka.domain.model.Transaction
import com.example.luka.domain.repository.AuthRepository

class saveTransactionUseCase(private val repository: AuthRepository){
    suspend operator fun invoke(transaction: Transaction): Boolean {
        return repository.saveTransaction(transaction)
    }
}
