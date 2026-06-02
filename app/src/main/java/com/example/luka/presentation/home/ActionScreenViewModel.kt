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

    // Funcionalidad de Recarga
    val operators = listOf("Claro", "Movistar", "Tigo", "Wom", "Virgin")
    var selectedOperator = mutableStateOf("Claro")
    
    var isPackageMode = mutableStateOf(false) // false = Recarga, true = Paquete
    
    val rechargePrices = listOf("3000", "5000", "10000", "20000", "50000")
    val packageOptions = listOf(
        "Day Pass - 1GB" to "3500",
        "Week Pass - 5GB" to "12000",
        "Month Pass - 20GB" to "40000",
        "Ultra Plan - 50GB" to "75000"
    )

    fun loadSavingGoals() {
        viewModelScope.launch {
            val goals = repository.getSavingGoals()
            savingGoals.clear()
            goals.let { savingGoals.addAll(it) }
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

    fun performRecharge(onSuccess: () -> Unit) {
        if (isLoading.value) return
        errorMessage.value = ""
        successMessage.value = ""

        val rechargeAmount = amount.value.toDoubleOrNull()
        if (rechargeAmount == null || rechargeAmount <= 0) {
            errorMessage.value = "Invalid amount"
            return
        }

        if (receiver.value.isEmpty()) {
            errorMessage.value = "Phone number required"
            return
        }

        isLoading.value = true

        viewModelScope.launch {
            val type = if (isPackageMode.value) "Package" else "Recharge"
            val result = repository.recharge(selectedOperator.value, receiver.value, rechargeAmount, type)
            isLoading.value = false
            if (result.first) {
                successMessage.value = result.second
                onSuccess()
            } else {
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
