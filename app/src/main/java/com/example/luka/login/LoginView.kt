package com.example.luka.login

import com.example.luka.R
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(viewModel: LoginViewModel, navController : NavController){
        Box(Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
            .padding(24.dp)
            ){
        Login(Modifier.align(Alignment.Center), viewModel, navController)
    }
}

@Composable
fun Login(modifier: Modifier, viewModel: LoginViewModel, navController: NavController) {

    val email : String by viewModel.email.observeAsState(initial = "")
    val password : String by viewModel.password.observeAsState(initial = "")
    val loginEnable: Boolean by viewModel.loginEnable.observeAsState(initial = false)
    val isLoading: Boolean by viewModel.isLoading.observeAsState(false)
    val showError: Boolean by viewModel.showError.observeAsState(false)
    val successMessage: String? by viewModel.showSuccessMessage.observeAsState(null)
    val coroutineScope = rememberCoroutineScope()

    if (isLoading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    }else{
        Column(modifier = modifier) {
            HeaderImage(Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = modifier.padding(16.dp))
            EmailField(email) { viewModel.onLoginChange(it, password) }
            Spacer(modifier = modifier.padding(4.dp))
            PasswordField(password) { viewModel.onLoginChange(email, it) }
            Spacer(modifier = modifier.padding(8.dp))
            ForgotPassword(Modifier.align(Alignment.End))
            Spacer(modifier = modifier.padding(16.dp))
            
            if (showError) {
                Text(
                    text = "Incorrect Password",
                    color = Color.Red,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 8.dp)
                )
            }

            if (successMessage != null) {
                Text(
                    text = successMessage!!,
                    color = Color.Green,
                    fontSize = 14.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 8.dp)
                )
            }

            LoginButton(loginEnable) {
                coroutineScope.launch {
                    viewModel.onLoginSelected()
                }
            }
            Spacer(modifier = Modifier.padding(8.dp))
            RegisterButton(Modifier.align(Alignment.CenterHorizontally)) {
                navController.navigate("register")
            }
        }
    }
}


@Composable
fun RegisterButton(modifier: Modifier, onRegisterSelected: () -> Unit) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Don't have an account?",
            color = Color.LightGray,
            fontSize = 14.sp
        )
        TextButton(onClick = { onRegisterSelected() }) {
            Text(
                text = "Sign up",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}



@Composable
fun LoginButton(loginEnable: Boolean, onLoginsSelected:() -> Unit){
    Button(onClick = { onLoginsSelected() },
         modifier = Modifier
            .fillMaxWidth()
            .height(48.dp),
    colors = ButtonDefaults.buttonColors(
        containerColor = Color(0xFF0D1B2A)


    ), enabled = loginEnable

    ) {
        Text(text = "Sign In",
            color = Color.White)
    }

}

@Composable
fun ForgotPassword(modifier: Modifier) {
    Text(
        text = "Forget Password?",
        modifier = modifier.clickable{ },
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFFFFFFFF)
        )
}

@Composable
fun PasswordField(password : String, onTextFieldChanged: (String)-> Unit){
    TextField(
        value = password, onValueChange = {onTextFieldChanged(it)},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text= "Password") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        singleLine = true,
        maxLines = 1,
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,

            cursorColor = Color.White,

            focusedContainerColor = Color(0xFF1B263B),
            unfocusedContainerColor = Color(0xFF1B263B),

            focusedPlaceholderColor = Color.LightGray,
            unfocusedPlaceholderColor = Color.Gray,

            focusedIndicatorColor = Color(0xFF415A77),
            unfocusedIndicatorColor = Color(0xFF415A77)
        )
    )
}
@Composable

fun EmailField(email: String, onTextFieldChanged: (String)-> Unit) {
      TextField(
        value = email, onValueChange = {onTextFieldChanged(it)},
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text= "Email") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        singleLine = true,
         maxLines = 1,
          colors = TextFieldDefaults.colors(
              focusedTextColor = Color.White,
              unfocusedTextColor = Color.White,

              cursorColor = Color.White,

              focusedContainerColor = Color(0xFF1B263B),
              unfocusedContainerColor = Color(0xFF1B263B),

              focusedPlaceholderColor = Color.LightGray,
              unfocusedPlaceholderColor = Color.Gray,

              focusedIndicatorColor = Color(0xFF415A77),
              unfocusedIndicatorColor = Color(0xFF415A77)
          )
      )
    }


@Composable
fun HeaderImage(modifier: Modifier){
  Image(
      painter = painterResource(id = R.drawable.dada),
      contentDescription = "Header",
      modifier = modifier
          .height(160.dp)
          .background(Color(0xFF0D1B2A))
          .padding(bottom = 12.dp)
  )
}
