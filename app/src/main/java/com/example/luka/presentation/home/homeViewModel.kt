package com.example.luka.presentation.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.luka.domain.model.Transaction

class HomeViewModel : ViewModel() {

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

        transactions.add(

            Transaction(
                title = title,
                amount = amount,
                date = "Today"
            )

        )
        balance.value -=
            amount
                .replace("-$","")
                .toDouble()

    }

}