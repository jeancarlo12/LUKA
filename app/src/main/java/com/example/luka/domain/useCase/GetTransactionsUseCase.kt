package com.example.luka.domain.useCase

import com.example.luka.domain.repository.AuthRepository
import com.example.luka.domain.model.Transaction

class GetTransactionsUseCase(private val repository: AuthRepository){
    operator fun invoke(onResult: (List<Transaction>)-> Unit){
        repository.getTransactions(onResult)
    }
}