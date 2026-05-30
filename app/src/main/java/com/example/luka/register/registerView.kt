package com.example.luka.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.approachLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.luka.register.registerViewModel

@Composable
fun registerScreen(registerView: registerViewModel, navController : NavController){

    var fullName by remember { mutableStateOf("") }

    Box(Modifier
        .fillMaxSize()
        .padding(16.dp)
    )
}

@Composable
fun Form(registerViewModel: registerViewModel ){
    Column(modifier = Modifier) {


        OutlinedTextField(
            value = registerViewModel.fullName.value,
            onValueChange = { registerViewModel.fullName.value = it },
            label = { Text("FullName") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
    }
}

