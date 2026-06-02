package com.example.luka.domain.repository

import com.example.luka.domain.model.Transaction
import com.example.luka.domain.model.User
import com.example.luka.domain.model.SavingGoal
import com.example.luka.domain.model.PaymentReminder

interface AuthRepository {
    fun registerUser(
        user: User,
        onResult: (Boolean, Int) -> Unit
    )
    suspend fun getTransactions(): List<Transaction>
    suspend fun getUserName(): String
    suspend fun getFullUserData(): User?
    fun logout()
    suspend fun getBalance(): Double
    suspend fun transfer(recipientEmail: String, amount: Double): Pair<Boolean, String>
    suspend fun recharge(operator: String, phoneNumber: String, amount: Double, type: String): Pair<Boolean, String>
    suspend fun saveTransaction(transaction: Transaction): Boolean
    suspend fun getSavingGoals(): List<SavingGoal>
    suspend fun updateSavingGoal(goalId: String, amountToAdd: Double): Boolean
    suspend fun adjustBalance(delta: Double): Boolean
    suspend fun addSavingGoal(goal: SavingGoal): Boolean
    suspend fun deleteSavingGoal(goalId: String, currentAmount: Double): Boolean
    suspend fun updatePhoneNumber(newPhone: String): Boolean
    suspend fun updateEmail(newEmail: String): Boolean
    suspend fun updatePassword(newPassword: String): Boolean
    suspend fun getPaymentReminders(): List<PaymentReminder>
    suspend fun addPaymentReminder(reminder: PaymentReminder): Boolean
    suspend fun deletePaymentReminder(reminderId: String): Boolean
}
