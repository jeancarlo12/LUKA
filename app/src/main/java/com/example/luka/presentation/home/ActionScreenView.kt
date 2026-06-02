package com.example.luka.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun ActionScreen(
    title: String,
    navController: NavController,
    homeViewModel: HomeViewModel,
    viewModel: ActionViewModel = viewModel()
) {
    val isRecharge = title.equals("Recharge", ignoreCase = true)
    
    LaunchedEffect(Unit) {
        viewModel.loadSavingGoals()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A)),
        contentAlignment = Alignment.Center
    ) {
        if (viewModel.isLoading.value) {
            CircularProgressIndicator(color = Color.White)
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = title,
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(30.dp))

            val textFieldColors = TextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                focusedContainerColor = Color(0xFF1B263B),
                unfocusedContainerColor = Color(0xFF1B263B),
                focusedLabelColor = Color.LightGray,
                unfocusedLabelColor = Color.Gray
            )

            if (isRecharge) {
                // Selector de Recarga vs Paquete
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    FilterChip(
                        selected = !viewModel.isPackageMode.value,
                        onClick = { viewModel.isPackageMode.value = false },
                        label = { Text("Recharge") },
                        colors = FilterChipDefaults.filterChipColors(labelColor = Color.White, selectedLabelColor = Color.White, selectedContainerColor = Color(0xFF415A77))
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    FilterChip(
                        selected = viewModel.isPackageMode.value,
                        onClick = { viewModel.isPackageMode.value = true },
                        label = { Text("Package") },
                        colors = FilterChipDefaults.filterChipColors(labelColor = Color.White, selectedLabelColor = Color.White, selectedContainerColor = Color(0xFF415A77))
                    )
                }

                // Selector de Operador
                Text("Select Operator:", color = Color.Gray, modifier = Modifier.align(Alignment.Start))
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    viewModel.operators.forEach { op ->
                        val isSelected = viewModel.selectedOperator.value == op
                        Button(
                            onClick = { viewModel.selectedOperator.value = op },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) Color(0xFF415A77) else Color(0xFF1B263B)
                            ),
                            contentPadding = PaddingValues(horizontal = 4.dp),
                            modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
                        ) {
                            Text(op, fontSize = 9.sp, color = Color.White)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
            }

            OutlinedTextField(
                value = viewModel.receiver.value,
                onValueChange = { viewModel.updateReceiver(it) },
                label = { Text(if (isRecharge) "Phone Number" else "Recipient Email") },
                enabled = !viewModel.isLoading.value,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors,
                keyboardOptions = KeyboardOptions(keyboardType = if (isRecharge) KeyboardType.Phone else KeyboardType.Email)
            )
            Spacer(modifier = Modifier.height(15.dp))

            if (isRecharge) {
                // Precios rápidos o paquetes
                Text(if (viewModel.isPackageMode.value) "Select Package:" else "Select Amount:", color = Color.Gray, modifier = Modifier.align(Alignment.Start))
                Spacer(modifier = Modifier.height(8.dp))
                
                if (viewModel.isPackageMode.value) {
                    // Lista de paquetes
                    viewModel.packageOptions.forEach { (desc, price) ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { viewModel.updateAmount(price) },
                            colors = CardDefaults.cardColors(containerColor = if (viewModel.amount.value == price) Color(0xFF415A77) else Color(0xFF1B263B))
                        ) {
                            Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(desc, color = Color.White)
                                Text("$$price", color = Color.Green, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    // Botones de precios rápidos
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        viewModel.rechargePrices.take(4).forEach { price ->
                            Button(
                                onClick = { viewModel.updateAmount(price) },
                                colors = ButtonDefaults.buttonColors(containerColor = if (viewModel.amount.value == price) Color(0xFF415A77) else Color(0xFF1B263B)),
                                modifier = Modifier.weight(1f).padding(horizontal = 2.dp)
                            ) {
                                Text("$$price", fontSize = 10.sp)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(15.dp))
            }

            OutlinedTextField(
                value = viewModel.amount.value,
                onValueChange = { viewModel.updateAmount(it) },
                label = { Text("Amount ($)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                enabled = !viewModel.isLoading.value,
                modifier = Modifier.fillMaxWidth(),
                colors = textFieldColors
            )

            Spacer(modifier = Modifier.height(20.dp))

            if (!isRecharge) {
                SmartSavingSection(viewModel, textFieldColors)
            }

            if (viewModel.errorMessage.value.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = viewModel.errorMessage.value,
                    color = Color.Red
                )
            }

            if (viewModel.successMessage.value.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = viewModel.successMessage.value,
                    color = Color.Green
                )
                LaunchedEffect(Unit) {
                    kotlinx.coroutines.delay(1500)
                    navController.popBackStack()
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    if (isRecharge) {
                        viewModel.performRecharge {
                            homeViewModel.loadBalance()
                            homeViewModel.loadTrasanctions()
                        }
                    } else {
                        viewModel.sendTransfer {
                            homeViewModel.loadBalance()
                            homeViewModel.loadTrasanctions()
                        }
                    }
                },
                enabled = !viewModel.isLoading.value,
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF415A77))
            ) {
                Text(text = if (isRecharge) "Confirm Transaction" else "Send", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun SmartSavingSection(viewModel: ActionViewModel, textFieldColors: TextFieldColors) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1B263B).copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Smart Saving (Round up)",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = viewModel.isSmartSavingEnabled.value,
                    onCheckedChange = { viewModel.isSmartSavingEnabled.value = it },
                    colors = SwitchDefaults.colors(checkedThumbColor = Color(0xFF415A77))
                )
            }

            if (viewModel.isSmartSavingEnabled.value) {
                val currentAmount = viewModel.amount.value.toDoubleOrNull() ?: 0.0
                val threshold = viewModel.savingThreshold.value.toDoubleOrNull() ?: 0.0

                if (currentAmount < threshold && viewModel.amount.value.isNotEmpty()) {
                    Text(
                        text = "⚠️ Smart saving only activates for transfers ≥ $${threshold.toInt()}",
                        color = Color(0xFFFFB74D),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))
                
                OutlinedTextField(
                    value = viewModel.savingThreshold.value,
                    onValueChange = { viewModel.updateThreshold(it) },
                    label = { Text("Minimum amount to save ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = textFieldColors,
                    singleLine = true
                )
                
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Save difference to:",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(8.dp))

                GoalSelectionItem(
                    name = "Keep it free (General Saving)",
                    isSelected = viewModel.selectedGoalId.value == null,
                    onClick = { viewModel.selectedGoalId.value = null }
                )
                
                if (viewModel.savingGoals.isEmpty()) {
                    Text(
                        text = "No goals created. Create one in Goals screen!",
                        color = Color(0xFF415A77),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(8.dp)
                    )
                }

                viewModel.savingGoals.forEach { goal ->
                    GoalSelectionItem(
                        name = "Goal: ${goal.name}",
                        isSelected = viewModel.selectedGoalId.value == goal.id,
                        onClick = { viewModel.selectedGoalId.value = goal.id }
                    )
                }
            }
        }
    }
}

@Composable
fun GoalSelectionItem(name: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp)
            .background(if (isSelected) Color(0xFF415A77).copy(alpha = 0.3f) else Color.Transparent)
            .padding(8.dp)
    ) {
        RadioButton(
            selected = isSelected,
            onClick = null,
            colors = RadioButtonDefaults.colors(selectedColor = Color.White, unselectedColor = Color.Gray)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = name, color = if (isSelected) Color.White else Color.Gray)
    }
}
