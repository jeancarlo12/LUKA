package com.example.luka.presentation.pay

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.luka.domain.model.PaymentReminder
import com.example.luka.presentation.home.BottomBar

@Composable
fun PayView(
    navController: NavController,
    viewModel: PayViewModel = viewModel()
) {
    var showForm by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.loadReminders()
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
            Text(
                text = "Payment Reminders",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(30.dp))

            if (viewModel.isLoading.value) {
                CircularProgressIndicator(color = Color.White)
            }

            viewModel.reminders.forEach { reminder ->
                ReminderCard(reminder) { viewModel.deleteReminder(reminder.id) }
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (viewModel.reminders.isEmpty() && !viewModel.isLoading.value) {
                Text("No reminders set.", color = Color.Gray)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { showForm = !showForm },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = if (showForm) Color.Gray else Color(0xFF415A77))
            ) {
                Text(if (showForm) "Cancel" else "+ Add Reminder")
            }

            if (showForm) {
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        val fieldColors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = Color(0xFF415A77)
                        )

                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            label = { Text("Service/Bill Name") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = fieldColors
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = amount,
                            onValueChange = { amount = it },
                            label = { Text("Amount ($)") },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = fieldColors
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = date,
                            onValueChange = { date = it },
                            label = { Text("Due Date (ex: 15th every month)") },
                            modifier = Modifier.fillMaxWidth(),
                            colors = fieldColors
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                val amt = amount.toDoubleOrNull() ?: 0.0
                                if (title.isNotEmpty() && amt > 0) {
                                    viewModel.addReminder(title, amt, date)
                                    showForm = false
                                    title = ""; amount = ""; date = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF415A77))
                        ) {
                            Text("Save Reminder")
                        }
                    }
                }
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
fun ReminderCard(reminder: PaymentReminder, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B)),
        shape = androidx.compose.foundation.shape.RoundedCornerShape(15.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Notifications, contentDescription = null, tint = Color(0xFF415A77))
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(reminder.title, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("Due: ${reminder.dueDate}", color = Color.Gray, fontSize = 14.sp)
                Text("Amount: $${reminder.amount.toInt()}", color = Color.LightGray, fontSize = 14.sp)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.6f))
            }
        }
    }
}
