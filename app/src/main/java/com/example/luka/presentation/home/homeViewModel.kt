package com.example.luka.presentation.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luka.data.repository.AuthRepositoryImpl
import com.example.luka.domain.model.Transaction
import com.example.luka.domain.useCase.GetBalanceUseCase
import com.example.luka.domain.useCase.GetTransactionsUseCase
import com.example.luka.domain.useCase.getUserNameUseCase
import com.example.luka.domain.useCase.logOutUserCase
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val getBalanceUseCase = GetBalanceUseCase(AuthRepositoryImpl())
    private val logOutUserCase = logOutUserCase(AuthRepositoryImpl())
    private val getUserNameUseCase = getUserNameUseCase(AuthRepositoryImpl())
    private val getTransactionsUseCase = GetTransactionsUseCase(AuthRepositoryImpl())

    var isBalancedVisible = mutableStateOf(true)
        private set
    var userName = mutableStateOf("")
    var balance = mutableStateOf(0.0)
    var transactions = mutableStateListOf<Transaction>()

    fun loadTrasanctions() {
        viewModelScope.launch {
            val loadedTransactions = getTransactionsUseCase()
            transactions.clear()
            val sortedTransactions = loadedTransactions.sortedByDescending { it.timestamp }
            transactions.addAll(sortedTransactions)
        }
    }

    fun loadUsername() {
        viewModelScope.launch {
            userName.value = getUserNameUseCase()
        }
    }

    fun logout() {
        logOutUserCase()
    }

    fun balancedVisibility() {
        isBalancedVisible.value = !isBalancedVisible.value
    }

    fun loadBalance() {
        viewModelScope.launch {
            balance.value = getBalanceUseCase()
        }
    }
}
