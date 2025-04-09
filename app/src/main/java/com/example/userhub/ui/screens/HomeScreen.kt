package com.example.userhub.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    navController: NavController,
    loadUser: (String) -> Unit,
    logout: () -> Unit,
    uid: String,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(uid) {
        loadUser(uid)
    }
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Welcome to UserHub!")

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            logout()
            navController.navigate("login") {
                popUpTo("home/${uid}") { inclusive = true } // Clears Home from back stack
            }
        }) {
            Text("Logout")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("profile")
        }) {
            Text("Profile")
        }
    }
}
