package com.example.luka.presentation.register

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController


@Composable
fun RegisterScreen(registerView: registerViewModel, navController : NavController, onRegisterSuccess: () -> Unit){
    Box(Modifier
        .fillMaxSize()
        .background(Color(0xFF0D1B2A))
        .padding(24.dp)
    ) {
        if (registerView.isLoading.value) {
            CircularProgressIndicator(Modifier.align(Alignment.Center), color = Color.White)
        }

        Column(Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Create Account",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            
            registerView.errorMessage.value?.let { 
                Text(text = it, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
            }

            Form(registerView, onRegisterSuccess)
        }
    }
}


@Composable
fun Form(registerViewModel: registerViewModel, onRegisterSuccess: () -> Unit ){
    val textFieldColors = TextFieldDefaults.colors(
        focusedTextColor = Color.White,
        unfocusedTextColor = Color.White,
        cursorColor = Color.White,
        focusedContainerColor = Color(0xFF1B263B),
        unfocusedContainerColor = Color(0xFF1B263B),
        focusedPlaceholderColor = Color.LightGray,
        unfocusedPlaceholderColor = Color.Gray,
        focusedIndicatorColor = Color(0xFF415A77),
        unfocusedIndicatorColor = Color(0xFF415A77),
        focusedLabelColor = Color.LightGray,
        unfocusedLabelColor = Color.Gray
    )

    Column(modifier = Modifier.fillMaxWidth()) {

        TextField(
            value = registerViewModel.fullName.value,
            onValueChange = { registerViewModel.fullName.value = it },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = registerViewModel.Email.value,
            onValueChange = { registerViewModel.Email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = registerViewModel.PhoneNumber.value,
            onValueChange = { 
                if (it.all { char -> char.isDigit() } && it.length <= 10) {
                    registerViewModel.PhoneNumber.value = it
                }
            },
            label = { Text("Phone Number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
            singleLine = true,
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = registerViewModel.DocumentNumber.value,
            onValueChange = { registerViewModel.DocumentNumber.value = it },
            label = { Text("Document Number") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = textFieldColors
        )

        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = registerViewModel.Password.value,
            onValueChange = { registerViewModel.Password.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )
        Spacer(modifier = Modifier.height(12.dp))

        TextField(
            value = registerViewModel.ConfirmPassword.value,
            onValueChange = { registerViewModel.ConfirmPassword.value = it },
            label = { Text("Confirm Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = textFieldColors,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                registerViewModel.register { success ->
                    if (success) {
                        onRegisterSuccess()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF415A77),
                contentColor = Color.White
            ),
            enabled = !registerViewModel.isLoading.value
        ) {
            Text(text = "Register", fontWeight = FontWeight.Bold)
        }
    }
}
