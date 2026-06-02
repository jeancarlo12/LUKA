package com.example.luka.data.repository

import android.util.Log
import com.example.luka.data.dataSource.FirebaseDataSource
import com.example.luka.domain.model.Transaction
import com.example.luka.domain.model.User
import com.example.luka.domain.model.SavingGoal
import com.example.luka.domain.model.PaymentReminder
import com.example.luka.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class AuthRepositoryImpl : AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val firebaseDataSource = FirebaseDataSource()

    init {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        firestore.firestoreSettings = settings
    }

    override fun registerUser(user: User, onResult: (Boolean, Int) -> Unit) {
        Log.d("LUKA_DEBUG", "Registering ${user.email}")
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    if (userId != null) {
                        val userMap = hashMapOf(
                            "fullName" to user.fullName,
                            "email" to user.email,
                            "phoneNumber" to "", // Inicializar vacío
                            "documentNumber" to user.documentNumber,
                            "password" to user.password,
                            "balance" to 3000000.0
                        )
                        firestore.collection("users").document(userId).set(userMap)
                            .addOnSuccessListener { onResult(true, 0) }
                            .addOnFailureListener { onResult(false, 2) }
                    } else {
                        onResult(false, 3)
                    }
                } else {
                    val errorCode = when (task.exception) {
                        is FirebaseAuthWeakPasswordException -> 4
                        is FirebaseAuthUserCollisionException -> 3
                        else -> 3
                    }
                    onResult(false, errorCode)
                }
            }
    }

    override suspend fun getTransactions(): List<Transaction> = firebaseDataSource.getTransactions()

    override suspend fun getUserName(): String {
        val data = firebaseDataSource.getUserData()
        return data?.get("fullName") as? String ?: "User"
    }

    override suspend fun getFullUserData(): User? = firebaseDataSource.getFullUserData()

    override fun logout() {
        auth.signOut()
    }

    override suspend fun getBalance(): Double {
        val data = firebaseDataSource.getUserData()
        return data?.get("balance") as? Double ?: 0.0
    }

    override suspend fun transfer(recipientEmail: String, amount: Double): Pair<Boolean, String> =
        firebaseDataSource.transfer(recipientEmail, amount)

    override suspend fun saveTransaction(transaction: Transaction): Boolean {
        val uid = firebaseDataSource.getCurrentUid() ?: return false
        return try {
            firestore.collection("users").document(uid).collection("transactions").add(transaction)
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getSavingGoals(): List<SavingGoal> = firebaseDataSource.getSavingGoals()

    override suspend fun updateSavingGoal(goalId: String, amountToAdd: Double): Boolean =
        firebaseDataSource.updateSavingGoal(goalId, amountToAdd)

    override suspend fun adjustBalance(delta: Double): Boolean = firebaseDataSource.adjustBalance(delta)

    override suspend fun addSavingGoal(goal: SavingGoal): Boolean = firebaseDataSource.addSavingGoal(goal)

    override suspend fun deleteSavingGoal(goalId: String, currentAmount: Double): Boolean =
        firebaseDataSource.deleteSavingGoal(goalId, currentAmount)

    override suspend fun updatePhoneNumber(newPhone: String): Boolean =
        firebaseDataSource.updatePhoneNumber(newPhone)

    override suspend fun updateEmail(newEmail: String): Boolean =
        firebaseDataSource.updateEmail(newEmail)

    override suspend fun updatePassword(newPassword: String): Boolean =
        firebaseDataSource.updatePassword(newPassword)

    override suspend fun getPaymentReminders(): List<PaymentReminder> =
        firebaseDataSource.getPaymentReminders()

    override suspend fun addPaymentReminder(reminder: PaymentReminder): Boolean =
        firebaseDataSource.addPaymentReminder(reminder)

    override suspend fun deletePaymentReminder(reminderId: String): Boolean =
        firebaseDataSource.deletePaymentReminder(reminderId)
}
