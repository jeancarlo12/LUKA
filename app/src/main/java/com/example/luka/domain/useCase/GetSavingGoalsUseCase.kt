package com.example.luka.domain.useCase

import com.example.luka.domain.model.SavingGoal
import com.example.luka.domain.repository.AuthRepository

class GetSavingGoalsUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(): List<SavingGoal> {
        return repository.getSavingGoals()
    }
}
