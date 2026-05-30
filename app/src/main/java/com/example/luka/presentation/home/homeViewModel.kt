package com.example.luka.presentation.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.luka.data.reposioRy.AuthRepositoryImpl
import com.example.luka.domain.model.Transaction
import com.example.luka.domain.useCase.GetBalanceUseCase
import com.example.luka.domain.useCase.GetTransactionsUseCase
import com.example.luka.domain.useCase.getUserNameUseCase
import com.example.luka.domain.useCase.logOutUserCase

class HomeViewModel : ViewModel() {

    private val getBalanceUseCase = GetBalanceUseCase(AuthRepositoryImpl())
    private val logOutUserCase = logOutUserCase(AuthRepositoryImpl())
    private val getUserNameUseCase = getUserNameUseCase(
        AuthRepositoryImpl())
    private val getTransactionsUseCase = GetTransactionsUseCase(
        AuthRepositoryImpl())

    var isBalancedVisible = mutableStateOf(true)
        private set
    var userName = mutableStateOf("")
    var balance = mutableStateOf(0.0)
    var transactions = mutableStateListOf<Transaction>()

    fun loadTrasanctions() {
        getTransactionsUseCase { loadedTransactions ->
            transactions.clear()
            // Sort by timestamp descending (newest first). 
            // Older ones with timestamp 0L will go to the bottom.
            val sortedTransactions = loadedTransactions.sortedByDescending { it.timestamp }
            transactions.addAll(sortedTransactions)
        }
    }
    fun loadUsername(){
        getUserNameUseCase {
            userName.value = it
        }
    }

    fun logout(){
        logOutUserCase()
    }

    fun balancedVisibility(){
        isBalancedVisible.value= !isBalancedVisible.value
    }
    fun loadBalance(){
        getBalanceUseCase{
            balance.value = it
        }
    }
}