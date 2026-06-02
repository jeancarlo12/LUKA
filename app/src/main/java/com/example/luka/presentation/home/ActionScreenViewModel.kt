package com.example.luka.presentation.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luka.data.repository.AuthRepositoryImpl
import com.example.luka.domain.model.SavingGoal
import com.example.luka.domain.repository.AuthRepository
import com.example.luka.domain.useCase.TransferUseCase
import kotlinx.coroutines.launch
import kotlin.math.ceil

class ActionViewModel : ViewModel() {
    private val repository: AuthRepository = AuthRepositoryImpl()
    private val transferUseCase = TransferUseCase(repository)

    var successMessage = mutableStateOf("")
    var errorMessage = mutableStateOf("")
    var receiver = mutableStateOf("")
    var amount = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    // Funcionalidad de Ahorro Inteligente
    var isSmartSavingEnabled = mutableStateOf(false)
    var savingGoals = mutableStateListOf<SavingGoal>()
    var selectedGoalId = mutableStateOf<String?>(null)
    var savingThreshold = mutableStateOf("50000")

    fun loadSavingGoals() {
        viewModelScope.launch {
            val goals = repository.getSavingGoals()
            savingGoals.clear()
            savingGoals.addAll(goals)
        }
    }

    fun updateReceiver(value: String) {
        receiver.value = value
    }

    fun updateAmount(value: String) {
        amount.value = value
    }

    fun updateThreshold(value: String) {
        savingThreshold.value = value
    }

    fun sendTransfer(onSuccess: () -> Unit) {
        if (isLoading.value) return

        errorMessage.value = ""
        successMessage.value = ""

        val transferAmount = amount.value.toDoubleOrNull()
        if (transferAmount == null || transferAmount <= 0) {
            errorMessage.value = "Invalid amount"
            return
        }

        if (receiver.value.isEmpty()) {
            errorMessage.value = "Recipient required"
            return
        }

        isLoading.value = true

        viewModelScope.launch {
            val result = transferUseCase(recipientEmail = receiver.value, amount = transferAmount)
            if (result.first) {
                val threshold = savingThreshold.value.toDoubleOrNull() ?: 0.0
                
                if (isSmartSavingEnabled.value && transferAmount >= threshold) {
                    processSmartSaving(transferAmount)
                    successMessage.value = "Transfer successful! Smart Saving applied."
                } else if (isSmartSavingEnabled.value && transferAmount < threshold) {
                    successMessage.value = "Transfer successful! (No saving: below threshold)"
                } else {
                    successMessage.value = result.second
                }
                isLoading.value = false
                onSuccess()
            } else {
                isLoading.value = false
                errorMessage.value = result.second
            }
        }
    }

    private suspend fun processSmartSaving(originalAmount: Double) {
        val roundedAmount = ceil(originalAmount / 1000.0) * 1000.0
        val spareChange = roundedAmount - originalAmount

        if (spareChange > 0) {
            val success = repository.adjustBalance(-spareChange)
            if (success && selectedGoalId.value != null) {
                repository.updateSavingGoal(selectedGoalId.value!!, spareChange)
            }
        }
    }
}
