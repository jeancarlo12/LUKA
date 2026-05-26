package com.example.luka.data.reposioRy

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
                            "password" to user.password // Nota: En producción no deberías guardar la pass en Firestore
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
    override fun saveTransaction(transaction: Transaction, onResult: (Boolean) -> Unit) {
        val uid = auth.currentUser?.uid

        if (uid == null) {
            onResult(false)
            return
        }

        firestore
            .collection("users")
            .document(uid)
            .collection("transactions")
            .add(transaction)
            .addOnSuccessListener {
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }
    fun getTransactions(onResult:(List<Transaction>) -> Unit){
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
    fun getUserName(onResult: (String) -> Unit){
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
}