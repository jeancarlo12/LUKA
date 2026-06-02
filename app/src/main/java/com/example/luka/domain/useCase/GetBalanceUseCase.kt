package com.example.luka.domain.useCase

import com.example.luka.domain.repository.AuthRepository

class GetBalanceUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): Double {
        return repository.getBalance()
    }
}
