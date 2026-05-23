package com.example.luka.domain

import com.example.luka.domain.model.User

class RegisterUseCase(private val repository: AuthRepository) {
    operator fun invoke(
        user: User,
        onResult: (Boolean, Int) -> Unit
    ) {
        repository.registerUser(user, onResult)
    }
}
