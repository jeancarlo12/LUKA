package com.example.luka.domain.useCase

import com.example.luka.domain.repository.AuthRepository

class getUserNameUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): String {
        return repository.getUserName()
    }
}
