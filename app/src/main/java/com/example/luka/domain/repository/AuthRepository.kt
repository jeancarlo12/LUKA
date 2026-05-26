package com.example.luka.domain.repository

import com.example.luka.domain.model.Transaction
import com.example.luka.domain.model.User

interface AuthRepository {
    fun registerUser(
        user: User,
        onResult: (Boolean, Int) -> Unit
    )
    fun saveTransaction(
        transaction: Transaction,
        onResult: (Boolean) -> Unit
    )
}