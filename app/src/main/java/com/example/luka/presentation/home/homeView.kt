package com.example.luka.presentation.home

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.focus.focusModifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun HomeView(viewModel: HomeViewModel = viewModel(),
        navController: NavController
) {
    LaunchedEffect(Unit) {
        viewModel.loadTrasanctions()
        viewModel.loadUsername()
        viewModel.loadBalance()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A)) 
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderSection(viewModel, navController)
        
        Spacer(modifier = Modifier.height(30.dp))

        BalanceCard(viewModel)

        Spacer(modifier = Modifier.height(40.dp))

        QuickActions(navController)

        Spacer(modifier = Modifier.weight(1f))

        RecentTransaction(viewModel)
        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                viewModel.addTransaction(
                    title = "Transfer",
                    amount = "-$50"
                )
            }
        ) {
            Text(
                text = "Add Transaction"
            )
        }
        BottomBar()
    }
}

@Composable
fun HeaderSection(viewModel: HomeViewModel, navController: NavController) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hello ${viewModel.userName.value}",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Welcome to luka",
                color = Color.Gray
            )
            Button(
                onClick = {
                    viewModel.logout()
                    navController.navigate("login") {
                        popUpTo(0)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.DarkGray
                )
            ) {
                Text(
                    text = "logout",
                    color = Color.White
                )
            }
            
        }
    }
}

@Composable
fun BalanceCard(viewModel: HomeViewModel) {
    val isVisible = viewModel.isBalancedVisible.value

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B263B)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Available balance",
                color = Color.LightGray,
            )
            Text(
                text = if(isVisible)
                    "$${viewModel.balance.value.toInt()}"
                else
                    "******",

                color = Color.White,
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
            Spacer(modifier= Modifier.width(8.dp))

            IconButton(onClick = {viewModel.balancedVisibility()}) {
                Icon(imageVector =
                if(isVisible)
                    Icons.Default.Visibility
                else
                    Icons.Default.VisibilityOff,
                    "Toggle balance",
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun QuickActions(navController: NavController){
    Column {
        Text(
            text = "Quick Actions",
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(
            modifier = Modifier.height(15.dp)
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            ActionItem(
                icon = Icons.Default.AttachMoney,
                text = "Transfer",
                onClick = {
                    navController.navigate(
                        "action/Transfer"
                    )
                }
            )

            ActionItem(
                icon = Icons.Default.CreditCard,
                text = "Pay",
                onClick = {
                    navController.navigate(
                        "action/Pay"
                    )
                }

            )

            ActionItem(
                icon = Icons.Default.AccountBalance,
                text = "Recharge",
                onClick = {
                    navController.navigate(
                        "action/recharge"
                    )
                }
            )

            ActionItem(
                icon = Icons.Default.MoreHoriz,
                text = "More",
                onClick = {
                    navController.navigate(
                        "action/More"
                    )
                }
            )
        }
    }
}

@Composable
fun ActionItem(icon: ImageVector, text: String,onClick:()->Unit={}) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(modifier = Modifier
            .size(
                width = 75.dp,
                height = 90.dp
            )
            .clickable{
                onClick()
            },

        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = text,
            color = Color.White,
            fontSize = 12.sp
        )
    }
}

@Composable
fun RecentTransaction(viewModel: HomeViewModel){
    Column{
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
            ){
            Text(
                text = "Recent Transactions",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "See All",
                color = Color.Gray,
                fontSize = 14.sp
            )
        }
        Spacer(modifier = Modifier.height(15.dp))

        viewModel.transactions.forEach {
            TransactionItem(
                title = it.title,
                amount = it.amount
            )
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun TransactionItem(title: String, amount: String){
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1B263B)
        )
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column {

                Text(
                    text = title,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Today",
                    color = Color.Gray,
                    fontSize = 12.sp
                )

            }

            Text(
                text = amount,
                color = if(amount.contains("+"))
                    Color.Green
                else
                    Color.Red,

                fontWeight = FontWeight.Bold
            )

        }

    }

}
@Composable
fun BottomBar(){

    Card(

        modifier = Modifier
            .fillMaxWidth()
            .height(70.dp),

        shape = RoundedCornerShape(30.dp),

        colors = CardDefaults.cardColors(
            containerColor =
                Color(0xFF1B263B)
        )

    ) {

        Row(

            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 25.dp),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            BottomItem(
                icon = Icons.Default.Home,
                text = "Home"
            )

            BottomItem(
                icon = Icons.Default.CreditCard,
                text = "Cards"
            )

            BottomItem(
                icon = Icons.Default.BarChart,
                text = "Activity"
            )

            BottomItem(
                icon = Icons.Default.Person,
                text = "Profile"
            )

        }

    }

}

@Composable
fun BottomItem(
    icon: ImageVector,
    text:String
){

    Column(

        horizontalAlignment =
            Alignment.CenterHorizontally

    ) {

        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = Color.White
        )

        Text(
            text = text,
            color = Color.White,
            fontSize = 10.sp
        )

    }

}



















