package com.example.luka.domain.useCase

import com.example.luka.domain.repository.AuthRepository
import com.example.luka.domain.model.Transaction

class GetTransactionsUseCase(private val repository: AuthRepository){
    suspend operator fun invoke(): List<Transaction> {
        return repository.getTransactions()
    }
}
