package com.example.luka.domain.useCase

import com.example.luka.domain.repository.AuthRepository
import kotlin.math.ceil

class ProcessRoundingUseCase(private val repository: AuthRepository) {
    operator fun invoke(
        amount: Double,
        selectedGoalId: String?, // Si es null, no se ahorra (queda libre)
        onResult: (Boolean, Double) -> Unit // éxito, cantidad redondeada
    ) {
        // Redondeo al siguiente mil (ej: 1250 -> 2000)
        // O si quieres redondear al siguiente entero, usa ceil.
        // El usuario es principiante, así que haremos un redondeo simple al siguiente "100" o "1000".
        // Hagamos redondeo al siguiente 1000 como es común en apps de ahorro.
        
        val roundedAmount = ceil(amount / 1000.0) * 1000.0
        val diff = roundedAmount - amount
        
        if (diff > 0 && selectedGoalId != null) {
            repository.updateSavingGoal(selectedGoalId, diff) { success ->
                onResult(success, diff)
            }
        } else {
            onResult(true, 0.0)
        }
    }
}
