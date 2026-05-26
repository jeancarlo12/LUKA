package com.example.luka.presentation.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.luka.data.reposioRy.AuthRepositoryImpl
import com.example.luka.domain.model.Transaction
import com.example.luka.domain.useCase.GetTransactionsUseCase
import com.example.luka.domain.useCase.saveTransactionUseCase

class HomeViewModel : ViewModel() {
    private val getTransactionsUseCase = GetTransactionsUseCase(
        AuthRepositoryImpl())
    private val saveTransactionUseCase = saveTransactionUseCase(
        AuthRepositoryImpl())

    var balance = mutableStateOf(3000000.0)
    var transactions = mutableStateListOf(

        Transaction(
            title = "Netflix",
            amount = "-$35",
            date = "Today"
        ),

        Transaction(
            title = "Spotify",
            amount = "-$15",
            date = "Today"
        )

    )

    fun addTransaction(
        title:String,
        amount:String
    ){

        val transaction = Transaction(
                title = title,
                amount = amount,
                date = "Today"
            )
        transactions.add(transaction)

        balance.value -=
            amount
                .replace("-$","")
                .toDouble()

        saveTransactionUseCase(transaction){
            success->
        }
    }
    fun loadTrasanctions(){
        getTransactionsUseCase{
            transactions.clear()
            transactions.addAll(it)
        }
    }
}