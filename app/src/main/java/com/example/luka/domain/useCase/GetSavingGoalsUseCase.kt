package com.example.luka.domain.useCase

import com.example.luka.domain.model.SavingGoal
import com.example.luka.domain.repository.AuthRepository

class GetSavingGoalsUseCase(private val repository: AuthRepository) {
    operator fun invoke(onResult: (List<SavingGoal>) -> Unit) {
        repository.getSavingGoals(onResult)
    }
}
