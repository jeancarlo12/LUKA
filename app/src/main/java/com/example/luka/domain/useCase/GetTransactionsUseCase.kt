package com.example.luka.domain.useCase

import com.example.luka.data.reposioRy.AuthRepositoryImpl
import com.example.luka.domain.model.Transaction

class GetTransactionsUseCase(private val repository: AuthRepositoryImpl){
    operator fun invoke(onResult: (List<Transaction>)-> Unit){
        repository.getTransactions(onResult)
    }
}