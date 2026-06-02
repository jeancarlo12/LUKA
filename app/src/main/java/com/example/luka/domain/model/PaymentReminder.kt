package com.example.luka.domain.model

data class PaymentReminder(
    val id: String = "",
    val title: String = "",
    val amount: Double = 0.0,
    val dueDate: String = ""
)
