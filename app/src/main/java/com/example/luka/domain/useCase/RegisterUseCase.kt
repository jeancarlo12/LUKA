package com.example.luka.domain.useCase

import com.example.luka.domain.AuthRepository
import com.example.luka.domain.model.User

class RegisterUseCase(private val repository: AuthRepository) {
    operator fun invoke(
        user: User,
        onResult: (Boolean, Int) -> Unit
    ) {
        repository.registerUser(user, onResult)
    }
}
