package com.example.luka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.luka.login.LoginScreen
import com.example.luka.login.LoginViewModel
import com.example.luka.navigation.AppNavigation
import com.example.luka.ui.theme.LUKATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LUKATheme {
                LoginScreen(LoginViewModel())
                AppNavigation()
            }
        }
    }
}

