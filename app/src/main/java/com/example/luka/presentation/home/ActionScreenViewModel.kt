package com.example.luka.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.luka.data.reposioRy.AuthRepositoryImpl

class ActionViewModel : ViewModel() {
    private val repository = AuthRepositoryImpl()

    var successMessage = mutableStateOf("")
    var errorMessage = mutableStateOf("")
    var receiver = mutableStateOf("")

    var amount = mutableStateOf("")

    fun updateReceiver(
        value: String
    ) {
        receiver.value = value
    }

    fun updateAmount(
        value: String
    ) {
        amount.value = value
    }

    fun sendTransfer(homeViewModel: HomeViewModel) {

        errorMessage.value = ""
        successMessage.value = ""

        if (receiver.value.isEmpty()) {

            errorMessage.value = "Recipient required"
            return
        }

        if (amount.value.isEmpty()) {

            errorMessage.value = "Amount required"
            return
        }

        val transferAmount =
            amount.value.toDoubleOrNull()

        if (transferAmount == null) {

            errorMessage.value = "Invalid amount"
            return
        }
        repository.transfer(recipientEmail = receiver.value, amount = transferAmount) { success, message ->
            if (success) {
                successMessage.value = message
                homeViewModel.loadBalance()

                homeViewModel.loadTrasanctions()
            }else{
                errorMessage.value = message
            }
        }

        if (transferAmount > homeViewModel.balance.value) {

            errorMessage.value = "Insufficient funds"
            return
        }

        homeViewModel.addTransaction(
            title = receiver.value,
            amount = "-$${amount.value}"
        )

    }
}