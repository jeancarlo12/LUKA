package com.example.luka.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.luka.login.LoginScreen
import com.example.luka.login.LoginViewModel
import com.example.luka.register.registerScreen
import com.example.luka.register.registerViewModel



@Composable
fun AppNavigation(){

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login"){
            LoginView(navController = navController)
        }
        composable("register"){
            registerView(navController = navController)

        }



    }


}

}
