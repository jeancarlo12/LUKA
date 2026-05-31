package com.example.luka.data.repository

import android.util.Log
import com.example.luka.domain.model.Transaction
import com.example.luka.domain.model.User
import com.example.luka.domain.repository.AuthRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

class AuthRepositoryImpl : AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    init {
        // Asegurar que Firestore no se quede esperando infinitamente si no hay conexión o permisos
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(true)
            .build()
        firestore.firestoreSettings = settings
    }

    override fun registerUser(
        user: User,
        onResult: (Boolean, Int) -> Unit
    ) {
        Log.d("LUKA_DEBUG", "1. AuthRepository: Inciando Auth para ${user.email}")

        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    Log.d("LUKA_DEBUG", "2. AuthRepository: Auth OK. UID: $userId")

                    if (userId != null) {
                        Log.d("LUKA_DEBUG", "3. AuthRepository: Intentando guardar en Firestore...")

                        // Usar un Map para asegurar compatibilidad total con Firestore
                        val userMap = hashMapOf(
                            "fullName" to user.fullName,
                            "email" to user.email,
                            "documentNumber" to user.documentNumber,
                            "password" to user.password, // Nota: En producción no deberías guardar la pass en Firestore
                            "balance" to 3000000.0
                        )

                        firestore.collection("users").document(userId)
                            .set(userMap)
                            .addOnSuccessListener {
                                Log.d("LUKA_DEBUG", "4. AuthRepository: ¡Firestore Guardado con Éxito!")
                                onResult(true, 0)
                            }
                            .addOnFailureListener { e ->
                                Log.e("LUKA_DEBUG", "4. AuthRepository: Error al guardar en Firestore", e)
                                onResult(false, 2)
                            }
                            .addOnCompleteListener { finalTask ->
                                if (!finalTask.isSuccessful) {
                                     Log.e("LUKA_DEBUG", "4. AuthRepository: Firestore Complete pero fallido")
                                     onResult(false, 2)
                                }
                            }
                    } else {
                        onResult(false, 3)
                    }
                } else {
                    val exception = task.exception
                    Log.e("LUKA_DEBUG", "2. AuthRepository: Fallo en Auth - ${exception?.message}")
                    val errorCode = when (exception) {
                        is FirebaseAuthWeakPasswordException -> 4
                        is FirebaseAuthUserCollisionException -> 3
                        else -> 3
                    }
                    onResult(false, errorCode)
                }
            }
    }

    override fun getTransactions(onResult:(List<Transaction>) -> Unit){
        val uid= auth.currentUser?.uid

        if (uid == null){
            onResult(emptyList())
            return
        }
        firestore
            .collection("users")
            .document(uid)
            .collection("transactions")

            .get()
            .addOnSuccessListener { result ->
                val transaction = result.documents.mapNotNull {
                    it.toObject(Transaction::class.java)
                }
                onResult(transaction)
            }
            .addOnFailureListener { onResult(emptyList()) }
    }
    override fun getUserName(onResult: (String) -> Unit){
        val uid=auth.currentUser?.uid

        if (uid == null){
            onResult("User")
            return
        }
        firestore
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener{
                val fullName =
                    it.getString("fullName")
                    onResult(fullName ?: "User")
            }
            .addOnFailureListener{
                onResult("User")
            }
    }
    override fun logout(){
        auth.signOut()
    }

    override fun getBalance(onResult: (Double) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onResult(0.0)
            return
        }
        firestore
            .collection("users")
            .document(uid)
            .get()
            .addOnSuccessListener {
                val balance = it.getDouble("balance") ?: 0.0
                onResult(balance)
            }
            .addOnFailureListener { onResult(0.0) }
    }

    override fun transfer(recipientEmail: String, amount: Double, onResult: (Boolean, String) -> Unit) {
        val senderUid = auth.currentUser?.uid

        if (senderUid == null) {
            onResult(false, "User not authenticated")
            return
        }

        firestore
            .collection("users")
            .whereEqualTo("email", recipientEmail)
            .get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) {
                    onResult(false, "User not found")
                    return@addOnSuccessListener
                }

                val receiverDocument = result.documents.first()
                val receiverUid = receiverDocument.id
                val receiverBalance = receiverDocument.getDouble("balance") ?: 0.0

                firestore
                    .collection("users")
                    .document(senderUid)
                    .get()
                    .addOnSuccessListener { senderDoc ->
                        val senderBalance = senderDoc.getDouble("balance") ?: 0.0

                        if (amount > senderBalance) {
                            onResult(false, "Insufficient funds")
                            return@addOnSuccessListener
                        }

                        val newSenderBalance = senderBalance - amount
                        val newReceiverBalance = receiverBalance + amount
                        val senderEmail = auth.currentUser?.email ?: "Unknown"

                        // Prepare transactions
                        val debitTransaction = Transaction(
                            title = recipientEmail,
                            amount = "-$${amount.toInt()}",
                            date = "Today",
                            timestamp = System.currentTimeMillis()
                        )

                        val creditTransaction = Transaction(
                            title = senderEmail,
                            amount = "+$${amount.toInt()}",
                            date = "Today",
                            timestamp = System.currentTimeMillis()
                        )

                        val batch = firestore.batch()
                        val senderRef = firestore.collection("users").document(senderUid)
                        val receiverRef = firestore.collection("users").document(receiverUid)
                        
                        batch.update(senderRef, "balance", newSenderBalance)
                        batch.update(receiverRef, "balance", newReceiverBalance)
                        
                        val senderTransRef = senderRef.collection("transactions").document()
                        val receiverTransRef = receiverRef.collection("transactions").document()
                        
                        batch.set(senderTransRef, debitTransaction)
                        batch.set(receiverTransRef, creditTransaction)

                        batch.commit()
                            .addOnSuccessListener {
                                onResult(true, "Transfer successful")
                            }
                            .addOnFailureListener { e ->
                                Log.e("LUKA_DEBUG", "Transfer batch failed", e)
                                onResult(false, "Transfer failed during final save")
                            }
                    }
                    .addOnFailureListener {
                        onResult(false, "Error fetching sender data")
                    }
            }
            .addOnFailureListener {
                onResult(false, "Transfer failed")
            }
    }

    override fun saveTransaction(transaction: Transaction, onResult: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid
        if (uid == null) {
            onResult(false)
            return
        }

        firestore.collection("users")
            .document(uid)
            .collection("transactions")
            .add(transaction)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
    }
}
