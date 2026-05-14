package com.example.luka

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
                AppNavigation()
            }
        }
    }
}

