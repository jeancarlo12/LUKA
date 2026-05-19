package com.example.luka.register

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun RegisterScreen(viewModel: RegisterViewModel, navController: NavController) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Form(viewModel)
    }
}

@Composable
fun Form(viewModel: RegisterViewModel) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = viewModel.fullName.value,
            onValueChange = { viewModel.fullName.value = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}
