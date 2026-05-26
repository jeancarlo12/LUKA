package com.example.luka.presentation.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ActionViewModel : ViewModel() {

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