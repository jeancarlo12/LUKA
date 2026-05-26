package com.example.luka.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.luka.presentation.home.ActionScreen
import com.example.luka.presentation.home.HomeView
import com.example.luka.presentation.home.HomeViewModel
import com.example.luka.presentation.login.LoginScreen
import com.example.luka.presentation.login.LoginViewModel
import com.example.luka.presentation.register.RegisterScreen
import com.example.luka.presentation.register.registerViewModel

@Composable
fun AppNavigation(){

    val navController = rememberNavController()
    val loginViewModel: LoginViewModel = viewModel()
    val registerViewModel: registerViewModel = viewModel()
    val homeViewModel: HomeViewModel = viewModel()

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
        composable("home"){
            HomeView(
                viewModel = homeViewModel,
                navController = navController
            )
        }
        composable(route = "action/{title}"
        ){
            backStackEntry ->
            val title =
                backStackEntry.arguments
                    ?.getString("title")
                    ?: ""

            ActionScreen(
                title = title,
                navController = navController,
                homeViewModel = homeViewModel
            )

        }
    }
}
