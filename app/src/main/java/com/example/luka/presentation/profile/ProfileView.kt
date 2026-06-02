package com.example.luka.presentation.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.luka.presentation.home.BottomBar

@Composable
fun ProfileView(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    var editPhone by remember { mutableStateOf("") }
    var editEmail by remember { mutableStateOf("") }
    var editPass by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    LaunchedEffect(viewModel.user.value) {
        viewModel.user.value?.let {
            editPhone = it.phoneNumber
            editEmail = it.email
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile",
                tint = Color.White,
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1B263B))
                    .padding(20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = viewModel.user.value?.fullName ?: "Loading...",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            if (viewModel.updateMessage.value.isNotEmpty()) {
                Text(
                    text = viewModel.updateMessage.value,
                    color = Color.Green,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Campo Celular (Solo números)
            ProfileEditField(
                label = "Phone Number",
                value = editPhone,
                onValueChange = { 
                    if (it.all { char -> char.isDigit() }) {
                        editPhone = it
                    }
                },
                icon = Icons.Default.Phone,
                onUpdate = { viewModel.updatePhone(editPhone) },
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Email
            ProfileEditField(
                label = "Email (Gmail)",
                value = editEmail,
                onValueChange = { editEmail = it },
                icon = Icons.Default.Email,
                onUpdate = { viewModel.updateEmail(editEmail) },
                keyboardType = KeyboardType.Email
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Contraseña
            ProfileEditField(
                label = "New Password",
                value = editPass,
                onValueChange = { editPass = it },
                icon = Icons.Default.Lock,
                onUpdate = { 
                    if (editPass.length >= 6) viewModel.updatePassword(editPass)
                    else viewModel.updateMessage.value = "Min 6 chars"
                },
                keyboardType = KeyboardType.Password
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.6f))
            ) {
                Text("Logout", color = Color.White)
            }

            Spacer(modifier = Modifier.height(100.dp))
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(24.dp)
        ) {
            BottomBar(navController)
        }
    }
}

@Composable
fun ProfileEditField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onUpdate: () -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = Color.Gray, fontSize = 12.sp)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                leadingIcon = { Icon(icon, contentDescription = null, tint = Color(0xFF415A77)) },
                keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedBorderColor = Color(0xFF415A77),
                    unfocusedBorderColor = Color(0xFF1B263B)
                ),
                singleLine = true
            )
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = onUpdate,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF415A77)),
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                Text("Update", fontSize = 12.sp)
            }
        }
    }
}
