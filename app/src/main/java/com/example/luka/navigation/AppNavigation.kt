package com.example.luka.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.luka.login.LoginScreen
import com.example.luka.login.LoginViewModel
import com.example.luka.register.registerScreen
import com.example.luka.register.registerViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            val loginViewModel: LoginViewModel = viewModel()
            LoginScreen(viewModel = loginViewModel, navController = navController)
        }
        composable("register") {
            val registerViewModel: registerViewModel = viewModel()
            registerScreen(registerView = registerViewModel, navController = navController)
        }
    }
}
