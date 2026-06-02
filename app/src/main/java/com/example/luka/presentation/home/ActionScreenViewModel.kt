package com.example.luka.presentation.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.luka.data.repository.AuthRepositoryImpl
import com.example.luka.domain.model.SavingGoal
import com.example.luka.domain.model.Transaction
import com.example.luka.domain.repository.AuthRepository
import kotlin.math.ceil

class ActionViewModel : ViewModel() {
    private val repository: AuthRepository = AuthRepositoryImpl()

    var successMessage = mutableStateOf("")
    var errorMessage = mutableStateOf("")
    var receiver = mutableStateOf("")
    var amount = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    // Funcionalidad de Ahorro Inteligente
    var isSmartSavingEnabled = mutableStateOf(false)
    var savingGoals = mutableStateListOf<SavingGoal>()
    var selectedGoalId = mutableStateOf<String?>(null) // null significa "Mantener libre"
    var savingThreshold = mutableStateOf("50000") // Tope para activar el ahorro

    fun loadSavingGoals() {
        repository.getSavingGoals { goals ->
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

        // Realizamos la transferencia principal
        repository.transfer(recipientEmail = receiver.value, amount = transferAmount) { success, message ->
            if (success) {
                // Revisamos si el ahorro inteligente está activo y si supera el tope
                val threshold = savingThreshold.value.toDoubleOrNull() ?: 0.0
                
                if (isSmartSavingEnabled.value && transferAmount >= threshold) {
                    processSmartSaving(transferAmount) {
                        isLoading.value = false
                        successMessage.value = "Transfer successful! Smart Saving applied."
                        onSuccess()
                    }
                } else if (isSmartSavingEnabled.value && transferAmount < threshold) {
                    // El ahorro estaba activo pero no llegó al tope
                    isLoading.value = false
                    successMessage.value = "Transfer successful! (No saving applied: amount below $${threshold.toInt()})"
                    onSuccess()
                } else {
                    isLoading.value = false
                    successMessage.value = message
                    onSuccess()
                }
            } else {
                isLoading.value = false
                errorMessage.value = message
            }
        }
    }

    private fun processSmartSaving(originalAmount: Double, onFinish: () -> Unit) {
        // Redondeo al siguiente 1000 (ej: 1250 -> 2000)
        val roundedAmount = ceil(originalAmount / 1000.0) * 1000.0
        val spareChange = roundedAmount - originalAmount

        if (spareChange > 0) {
            // Descontamos el "vuelto" del balance principal
            repository.adjustBalance(-spareChange) { success ->
                if (success) {
                    if (selectedGoalId.value != null) {
                        // Si el usuario eligió una meta, guardamos el dinero allí
                        repository.updateSavingGoal(selectedGoalId.value!!, spareChange) {
                            onFinish()
                        }
                    } else {
                        // Dinero queda "libre" (ya se descontó del balance)
                        onFinish()
                    }
                } else {
                    onFinish()
                }
            }
        } else {
            onFinish()
        }
    }
}
