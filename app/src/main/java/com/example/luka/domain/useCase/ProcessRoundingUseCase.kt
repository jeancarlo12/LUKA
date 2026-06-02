package com.example.luka.domain.useCase

import com.example.luka.domain.repository.AuthRepository
import kotlin.math.ceil

class ProcessRoundingUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(
        amount: Double,
        selectedGoalId: String?
    ): Double {
        val roundedAmount = ceil(amount / 1000.0) * 1000.0
        val diff = roundedAmount - amount
        
        if (diff > 0 && selectedGoalId != null) {
            val success = repository.updateSavingGoal(selectedGoalId, diff)
            return if (success) diff else 0.0
        }
        return 0.0
    }
}
