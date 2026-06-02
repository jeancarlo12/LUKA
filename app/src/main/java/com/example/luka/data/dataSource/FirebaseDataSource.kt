package com.example.luka.data.dataSource

import com.example.luka.domain.model.SavingGoal
import com.example.luka.domain.model.Transaction
import com.example.luka.domain.model.User
import com.example.luka.domain.model.PaymentReminder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FirebaseDataSource {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    fun getCurrentUid(): String? = auth.currentUser?.uid
    fun getCurrentEmail(): String? = auth.currentUser?.email

    suspend fun getTransactions(): List<Transaction> {
        val uid = getCurrentUid() ?: return emptyList()
        return try {
            val result = firestore.collection("users")
                .document(uid)
                .collection("transactions")
                .get()
                .await()
            result.documents.mapNotNull { it.toObject(Transaction::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getFullUserData(): User? {
        val uid = getCurrentUid() ?: return null
        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updatePhoneNumber(newPhone: String): Boolean {
        val uid = getCurrentUid() ?: return false
        return try {
            firestore.collection("users").document(uid).update("phoneNumber", newPhone).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updateEmail(newEmail: String): Boolean {
        val user = auth.currentUser ?: return false
        val uid = user.uid
        return try {
            // Update in Auth
            user.updateEmail(newEmail).await()
            // Update in Firestore
            firestore.collection("users").document(uid).update("email", newEmail).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun updatePassword(newPassword: String): Boolean {
        val user = auth.currentUser ?: return false
        return try {
            user.updatePassword(newPassword).await()
            // Optional: Update in Firestore if you are storing it there (not recommended for security)
            firestore.collection("users").document(user.uid).update("password", newPassword).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getUserData(): Map<String, Any?>? {
        val uid = getCurrentUid() ?: return null
        return try {
            val snapshot = firestore.collection("users").document(uid).get().await()
            snapshot.data
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getSavingGoals(): List<SavingGoal> {
        val uid = getCurrentUid() ?: return emptyList()
        return try {
            val result = firestore.collection("users")
                .document(uid)
                .collection("savingGoals")
                .get()
                .await()
            result.documents.mapNotNull { doc ->
                doc.toObject(SavingGoal::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun updateSavingGoal(goalId: String, amountToAdd: Double): Boolean {
        val uid = getCurrentUid() ?: return false
        val goalRef = firestore.collection("users").document(uid).collection("savingGoals").document(goalId)
        return try {
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(goalRef)
                val currentAmount = snapshot.getDouble("currentAmount") ?: 0.0
                transaction.update(goalRef, "currentAmount", currentAmount + amountToAdd)
            }.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun adjustBalance(delta: Double): Boolean {
        val uid = getCurrentUid() ?: return false
        val userRef = firestore.collection("users").document(uid)
        return try {
            firestore.runTransaction { transaction ->
                val snapshot = transaction.get(userRef)
                val currentBalance = snapshot.getDouble("balance") ?: 0.0
                transaction.update(userRef, "balance", currentBalance + delta)
            }.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun transfer(recipientEmail: String, amount: Double): Pair<Boolean, String> {
        val senderUid = getCurrentUid() ?: return Pair(false, "User not authenticated")
        val senderEmail = getCurrentEmail()
        
        if (recipientEmail.equals(senderEmail, ignoreCase = true)) {
            return Pair(false, "You cannot transfer money to yourself")
        }
        
        return try {
            val query = firestore.collection("users").whereEqualTo("email", recipientEmail).get().await()
            if (query.isEmpty) return Pair(false, "User not found")
            
            val receiverDoc = query.documents.first()
            val receiverUid = receiverDoc.id
            val receiverBalance = receiverDoc.getDouble("balance") ?: 0.0
            
            val senderDoc = firestore.collection("users").document(senderUid).get().await()
            val senderBalance = senderDoc.getDouble("balance") ?: 0.0
            
            if (amount > senderBalance) return Pair(false, "Insufficient funds")
            
            val senderEmail = getCurrentEmail() ?: "Unknown"
            
            // Formatear la fecha actual
            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            
            val debitTrans = Transaction(recipientEmail, "-$${amount.toInt()}", currentDate, System.currentTimeMillis())
            val creditTrans = Transaction(senderEmail, "+$${amount.toInt()}", currentDate, System.currentTimeMillis())
            
            firestore.runBatch { batch ->
                val senderRef = firestore.collection("users").document(senderUid)
                val receiverRef = firestore.collection("users").document(receiverUid)
                
                batch.update(senderRef, "balance", senderBalance - amount)
                batch.update(receiverRef, "balance", receiverBalance + amount)
                
                batch.set(senderRef.collection("transactions").document(), debitTrans)
                batch.set(receiverRef.collection("transactions").document(), creditTrans)
            }.await()
            
            Pair(true, "Transfer successful")
        } catch (e: Exception) {
            Pair(false, "Error: ${e.message}")
        }
    }

    suspend fun recharge(operator: String, phoneNumber: String, amount: Double, type: String): Pair<Boolean, String> {
        val uid = getCurrentUid() ?: return Pair(false, "User not authenticated")
        return try {
            val userRef = firestore.collection("users").document(uid)
            val snapshot = userRef.get().await()
            val currentBalance = snapshot.getDouble("balance") ?: 0.0

            if (amount > currentBalance) return Pair(false, "Insufficient funds")

            val currentDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())
            val rechargeTrans = Transaction(
                title = "$type $operator ($phoneNumber)",
                amount = "-$${amount.toInt()}",
                date = currentDate,
                timestamp = System.currentTimeMillis()
            )

            firestore.runBatch { batch ->
                batch.update(userRef, "balance", currentBalance - amount)
                batch.set(userRef.collection("transactions").document(), rechargeTrans)
            }.await()

            Pair(true, "Recharge of $${amount.toInt()} to $operator successful")
        } catch (e: Exception) {
            Pair(false, "Error: ${e.message}")
        }
    }

    suspend fun addSavingGoal(goal: SavingGoal): Boolean {
        val uid = getCurrentUid() ?: return false
        return try {
            firestore.collection("users").document(uid).collection("savingGoals").add(goal).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deleteSavingGoal(goalId: String, currentAmount: Double): Boolean {
        val uid = getCurrentUid() ?: return false
        val userRef = firestore.collection("users").document(uid)
        val goalRef = userRef.collection("savingGoals").document(goalId)
        
        return try {
            firestore.runTransaction { transaction ->
                val userSnap = transaction.get(userRef)
                val balance = userSnap.getDouble("balance") ?: 0.0
                transaction.update(userRef, "balance", balance + currentAmount)
                transaction.delete(goalRef)
            }.await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getPaymentReminders(): List<PaymentReminder> {
        val uid = getCurrentUid() ?: return emptyList()
        return try {
            val result = firestore.collection("users")
                .document(uid)
                .collection("paymentReminders")
                .get()
                .await()
            result.documents.mapNotNull { doc ->
                doc.toObject(PaymentReminder::class.java)?.copy(id = doc.id)
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun addPaymentReminder(reminder: PaymentReminder): Boolean {
        val uid = getCurrentUid() ?: return false
        return try {
            firestore.collection("users").document(uid).collection("paymentReminders").add(reminder).await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun deletePaymentReminder(reminderId: String): Boolean {
        val uid = getCurrentUid() ?: return false
        return try {
            firestore.collection("users").document(uid).collection("paymentReminders").document(reminderId).delete().await()
            true
        } catch (e: Exception) {
            false
        }
    }
}
