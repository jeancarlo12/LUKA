package com.example.luka.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.luka.login.LoginScreen
import com.example.luka.login.LoginViewModel
import com.example.luka.register.RegisterScreen
import com.example.luka.register.registerViewModel




@Composable
fun AppNavigation(){

    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()
    val registerViewModel: registerViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login"){
            LoginScreen(
                viewModel = loginViewModel,
                navController = navController
            )
        }
        composable("register"){
            RegisterScreen(
                registerView = registerViewModel,
                navController = navController,
                onRegisterSuccess = {
                    loginViewModel.showSuccess("Registration successful!")
                    navController.navigate("login") {
                        popUpTo("register") { inclusive = true }
                    }
                }
            )

        }
    }
}
