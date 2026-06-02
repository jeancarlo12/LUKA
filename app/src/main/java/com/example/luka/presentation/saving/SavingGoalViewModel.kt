package com.example.luka.presentation.saving

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.luka.data.repository.AuthRepositoryImpl
import com.example.luka.domain.model.SavingGoal
import com.example.luka.domain.repository.AuthRepository
import kotlinx.coroutines.launch

class SavingGoalsViewModel : ViewModel() {
    private val repository: AuthRepository = AuthRepositoryImpl()
    val goals = mutableStateListOf<SavingGoal>()
    val isLoading = mutableStateOf(false)

    fun loadGoals() {
        isLoading.value = true
        viewModelScope.launch {
            val loadedGoals = repository.getSavingGoals()
            goals.clear()
            goals.addAll(loadedGoals)
            isLoading.value = false
        }
    }

    fun addGoal(name: String, target: Double) {
        val newGoal = SavingGoal(name = name, targetAmount = target, currentAmount = 0.0)
        viewModelScope.launch {
            val success = repository.addSavingGoal(newGoal)
            if (success) {
                loadGoals()
            }
        }
    }

    fun deleteGoal(goal: SavingGoal) {
        viewModelScope.launch {
            val success = repository.deleteSavingGoal(goal.id, goal.currentAmount)
            if (success) {
                loadGoals()
            }
        }
    }
}
