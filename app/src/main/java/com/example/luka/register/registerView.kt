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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.approachLayout
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun registerScreen(registerView: registerViewModel, navController : NavController){
    Box(Modifier
        .fillMaxSize()
        .padding(16.dp)
    ){
        singUp(Modifier.align(Alignment.Center), registerView)
    }
}

@Composable
fun singUp(modifier: Modifier, registerView: registerViewModel){

    val fullName : String by remember { mutableStateOf(" ") }



}

@Preview
@Composable
fun Form(fullName : String  ){


    Column(modifier = Modifier) {
        Form(fullName)
    }








        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it },
            label = { Text("FullName") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

}


