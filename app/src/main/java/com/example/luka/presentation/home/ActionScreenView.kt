package com.example.luka.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ActionScreen(
    title:String,
    navController: NavController,
    homeViewModel: HomeViewModel,
    viewModel: ActionViewModel = viewModel()
){

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color(0xFF0D1B2A)
            ),

        contentAlignment =
            Alignment.Center
    ){
        Column(
            horizontalAlignment =
                Alignment.CenterHorizontally
        ){
            Text(
                text = title,
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(

                value = viewModel.receiver.value,

                onValueChange = {
                    viewModel.updateReceiver(it)
                },

                label = {
                    Text("Recipient Email")
                }

            )
            Spacer(modifier = Modifier.height(15.dp))

            OutlinedTextField(
                value =
                    viewModel.amount.value,
                onValueChange = {
                    viewModel.updateAmount(it)
                },

                label = {
                    Text(
                        "Amount"
                    )

                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            if(viewModel.errorMessage.value.isNotEmpty()){
                Spacer(modifier= Modifier.height(10.dp))
                Text(
                    text = viewModel.errorMessage.value,
                    color = Color.Red
                )
            }

            Spacer(
                modifier = Modifier.height(20.dp)
            )

            Button(onClick = {
                    viewModel.sendTransfer(homeViewModel)
                if(viewModel.errorMessage.value.isEmpty()){
                    navController.popBackStack()
                }

                }
            ) {
                Text(
                    text = "Send"
                )

            }

        }

    }

}