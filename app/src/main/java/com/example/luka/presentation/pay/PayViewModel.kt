package com.example.luka.presentation.pay

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luka.data.repository.AuthRepositoryImpl
import com.example.luka.domain.model.PaymentReminder
import com.example.luka.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class PayViewModel : ViewModel() {
    private val repository: AuthRepository = AuthRepositoryImpl()
    
    val reminders = mutableStateListOf<PaymentReminder>()
    val isLoading = mutableStateOf(false)
    val message = mutableStateOf("")

    fun loadReminders() {
        isLoading.value = true
        viewModelScope.launch {
            val loaded = repository.getPaymentReminders()
            reminders.clear()
            reminders.addAll(loaded)
            isLoading.value = false
        }
    }

    fun addReminder(title: String, amount: Double, date: String) {
        val reminder = PaymentReminder(title = title, amount = amount, dueDate = date)
        viewModelScope.launch {
            val success = repository.addPaymentReminder(reminder)
            if (success) {
                message.value = "Reminder added!"
                loadReminders()
            } else {
                message.value = "Error adding reminder"
            }
        }
    }

    fun deleteReminder(reminderId: String) {
        viewModelScope.launch {
            val success = repository.deletePaymentReminder(reminderId)
            if (success) {
                loadReminders()
            }
        }
    }
}
