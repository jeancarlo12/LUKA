package com.example.luka.domain.useCase

import com.example.luka.domain.model.Transaction
import com.example.luka.domain.repository.AuthRepository

class saveTransactionUseCase(private val repository: AuthRepository){
    operator fun invoke(
        transaction: Transaction,
        onResult: (Boolean)-> Unit

    ){
        repository.saveTransaction(transaction,onResult)
    }
}
