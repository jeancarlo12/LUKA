package com.example.luka.domain.model

data class SavingGoal(
    val id: String = "",
    val name: String = "",
    val targetAmount: Double = 0.0,
    val currentAmount: Double = 0.0
)
