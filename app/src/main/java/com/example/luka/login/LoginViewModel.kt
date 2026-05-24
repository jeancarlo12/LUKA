package com.example.luka.login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

class LoginViewModel : ViewModel() {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _loginEnable = MutableLiveData<Boolean>()
    val loginEnable: LiveData<Boolean> = _loginEnable

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _showError = MutableLiveData<Boolean>()
    val showError: LiveData<Boolean> = _showError

    private val _showSuccessMessage = MutableLiveData<String?>()
    val showSuccessMessage: LiveData<String?> = _showSuccessMessage

    fun onLoginChange(email: String, password: String) {
        _email.value = email
        _password.value = password
        _loginEnable.value = isValidEmail(email) && isValidPassword(password)
        _showError.value = false
    }

    private fun isValidEmail(email: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(email).matches()

    private fun isValidPassword(password: String): Boolean = password.length >= 6

    fun showSuccess(message: String) {
        _showSuccessMessage.value = message
    }

    fun clearSuccessMessage() {
        _showSuccessMessage.value = null
    }

    fun onLoginSelected(onLoginSuccess: () -> Unit) {
        val emailVal = _email.value ?: return
        val passwordVal = _password.value ?: return

        _isLoading.value = true
        _showError.value = false
        clearSuccessMessage()

        auth.signInWithEmailAndPassword(emailVal, passwordVal)
            .addOnCompleteListener { task ->
                _isLoading.value = false
                if (task.isSuccessful) {
                    Log.d("LUKA_DEBUG", "Login successful for $emailVal")
                    onLoginSuccess()
                } else {
                    Log.e("LUKA_DEBUG", "Login failed: ${task.exception?.message}")
                    _showError.value = true
                }
            }
    }
}
