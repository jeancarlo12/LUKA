package com.example.luka.domain

import com.example.luka.domain.model.User

interface AuthRepository {
    fun registerUser(
        user: User,
        onResult: (Boolean, Int) -> Unit
    )
}
