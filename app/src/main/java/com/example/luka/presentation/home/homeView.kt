package com.example.luka.presentation.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material3.Icon
import com.google.firebase.firestore.Transaction
import java.time.temporal.TemporalAmount
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Person
@Composable
fun HomeView(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A)) 
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderSection()
        
        Spacer(modifier = Modifier.height(30.dp))

        BalanceCard()

        Spacer(modifier = Modifier.height(40.dp))

        quickActions()

        Spacer(modifier = Modifier.weight(1f))

        RecentTransaction()
        Spacer(modifier = Modifier.weight(1f))

        BottomBar()
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "Hi Rafael",
                color = Color.White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Welcome to luka",
                color = Color.Gray
            )
        }
    }
}

@Composable
fun BalanceCard() {
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
                text = "3.000.000",
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
            Text(
                text = "**** **** **** 1204",
                color = Color.Gray,
            )
        }
    }
}

@Composable
fun quickActions(){
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
                text = "Transfer"
            )

            ActionItem(
                icon = Icons.Default.CreditCard,
                text = "Pay"
            )

            ActionItem(
                icon = Icons.Default.AccountBalance,
                text = "Recharge"
            )

            ActionItem(
                icon = Icons.Default.MoreHoriz,
                text = "More"
            )
        }
    }
}

@Composable
fun ActionItem(icon: ImageVector, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            modifier = Modifier.size(70.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1B263B)
            )
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
fun RecentTransaction(){
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

        TransactionItem(
            title = "NETFLIX",
            amount = "-$15"
        )
        Spacer(modifier = Modifier.height(10.dp))

        TransactionItem(
            title = "PIZZA",
            amount = "-$15"
        )
        Spacer(modifier = Modifier.height(10.dp))

        TransactionItem(
            title = "SALARY",
            amount = "+$2.000"
        )


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


















@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomePreview() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D1B2A))
            .padding(24.dp)
    ) {
        HeaderSection()
        Spacer(modifier = Modifier.height(30.dp))
        BalanceCard()
        Spacer(modifier = Modifier.height(30.dp))
        quickActions()
        Spacer(modifier = Modifier.height(30.dp))
        RecentTransaction()
        Spacer(modifier = Modifier.height(30.dp))
        BottomBar()
    }
}
