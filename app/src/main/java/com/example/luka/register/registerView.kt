package com.example.luka.register

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun RegisterScreen(registerView: registerViewModel, navController : NavController, onRegisterSuccess: () -> Unit){
    Box(Modifier.fillMaxSize()) {
        if (registerView.isLoading.value) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }

        Column(Modifier
            .fillMaxSize()
            .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Create Account", modifier = Modifier.padding(bottom = 16.dp))
            Form(registerView, navController, onRegisterSuccess)
        }
    }
}


@Composable
fun Form(registerViewModel: registerViewModel, navController: NavController, onRegisterSuccess: () -> Unit ){
    Column(modifier = Modifier) {


        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = registerViewModel.fullName.value,
            onValueChange = { registerViewModel.fullName.value = it },
            label = { Text("FullName") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            maxLines = 1,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedPlaceholderColor = Color.LightGray,
                unfocusedPlaceholderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = registerViewModel.Email.value,
            onValueChange = { registerViewModel.Email.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedPlaceholderColor = Color.LightGray,
                unfocusedPlaceholderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = registerViewModel.DocumentNumber.value,
            onValueChange = { registerViewModel.DocumentNumber.value = it },
            label = { Text("DocumentNumber") },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedPlaceholderColor = Color.LightGray,
                unfocusedPlaceholderColor = Color.LightGray
            )


        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = registerViewModel.Password.value,
            onValueChange = { registerViewModel.Password.value = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedPlaceholderColor = Color.LightGray,
                unfocusedPlaceholderColor = Color.LightGray
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = registerViewModel.ConfirmPassword.value,
            onValueChange = { registerViewModel.ConfirmPassword.value = it },
            label = { Text("ConfirmPassword") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedPlaceholderColor = Color.LightGray,
                unfocusedPlaceholderColor = Color.LightGray
            )
        )

        Spacer(modifier = Modifier.height(16.dp))



        Button(onClick = {
            android.util.Log.d("RegisterForm", "Button clicked")
            registerViewModel.register(
                fullName = registerViewModel.fullName.value,
                Email = registerViewModel.Email.value,
                DocumentNumber = registerViewModel.DocumentNumber.value,
                Password = registerViewModel.Password.value,
                ConfirmPassword = registerViewModel.ConfirmPassword.value,
                OnResult = { success, result ->
                    if (success) {
                        onRegisterSuccess()
                    }
                }
            )
        } ,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Gray
            ),

            ) {
                Text(text = "Register",
                    color = Color.Black)
            }

        }

    }


