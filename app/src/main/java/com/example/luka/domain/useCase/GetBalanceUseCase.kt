package com.example.luka.domain.useCase

import com.example.luka.domain.repository.AuthRepository

class GetBalanceUseCase(private val repository: AuthRepository) {
    operator fun invoke(onResult: (Double) -> Unit) {
        repository.getUserBalance(onResult)
    }
}