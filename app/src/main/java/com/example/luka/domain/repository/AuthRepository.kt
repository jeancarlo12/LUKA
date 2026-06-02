package com.example.luka.domain.repository

import com.example.luka.domain.model.Transaction
import com.example.luka.domain.model.User
import com.example.luka.domain.model.SavingGoal

interface AuthRepository {
    fun registerUser(
        user: User,
        onResult: (Boolean, Int) -> Unit
    )
    fun getTransactions(onResult: (List<Transaction>) -> Unit)
    fun getUserName(onResult: (String) -> Unit)
    fun logout()
    fun getBalance(onResult: (Double) -> Unit)
    fun transfer(recipientEmail: String, amount: Double, onResult: (Boolean, String) -> Unit)
    fun saveTransaction(transaction: Transaction, onResult: (Boolean) -> Unit)
    fun getSavingGoals(onResult: (List<SavingGoal>) -> Unit)
    fun updateSavingGoal(goalId: String, amountToAdd: Double, onResult: (Boolean) -> Unit)
    fun adjustBalance(delta: Double, onResult: (Boolean) -> Unit)
    fun addSavingGoal(goal: SavingGoal, onResult: (Boolean) -> Unit)
    fun deleteSavingGoal(goalId: String, currentAmount: Double, onResult: (Boolean) -> Unit)
}