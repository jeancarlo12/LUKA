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
    fun getTransactions(onResult: (List<Transaction>) -> Unit)
    fun getUserName(onResult: (String) -> Unit)
    fun logout()
    fun getBalance(onResult: (Double) -> Unit)
    fun transfer(recipientEmail: String, amount: Double, onResult: (Boolean, String) -> Unit)
    fun getUserBalance(onResult: (Double) -> Unit)
}