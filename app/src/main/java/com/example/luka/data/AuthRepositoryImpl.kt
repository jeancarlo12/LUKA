package com.example.luka.data

import android.util.Log
import com.example.luka.domain.AuthRepository
import com.example.luka.domain.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthRepositoryImpl : AuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    override fun registerUser(
        user: User,
        onResult: (Boolean, Int) -> Unit
    ) {
        Log.d("AuthRepository", "Starting registration for: ${user.email}")
        auth.createUserWithEmailAndPassword(user.email, user.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid
                    Log.d("AuthRepository", "Auth successful, UID: $userId")
                    if (userId != null) {
                        firestore.collection("users").document(userId)
                            .set(user)
                            .addOnSuccessListener {
                                Log.d("AuthRepository", "Firestore write successful")
                                onResult(true, 0)
                            }
                            .addOnFailureListener { e ->
                                Log.e("AuthRepository", "Firestore error", e)
                                onResult(false, 2) // Firestore error
                            }
                    }
                } else {
                    Log.e("AuthRepository", "Auth error: ${task.exception?.message}")
                    onResult(false, 3) // Auth error
                }
            }
    }
}
