package com.example.userhub.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.userhub.auth.AuthViewModel
import com.example.userhub.ui.ProfileViewModel
import com.example.userhub.ui.screens.HomeScreen
import com.example.userhub.ui.screens.LoginScreen
import com.example.userhub.ui.screens.ProfileScreen
import com.example.userhub.ui.screens.SignupScreen

sealed class AuthScreen(val route: String) {
    data object Login : AuthScreen("login")
    data object Signup : AuthScreen("signup")
    data object Home : AuthScreen("home")
}

@Composable
fun AuthNavGraph(navController: NavHostController) {
    val authViewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory)
    val profileViewModel: ProfileViewModel = viewModel(factory = ProfileViewModel.Factory)
    val authState = authViewModel.authState.collectAsState()
    val userState = profileViewModel.user
    NavHost(navController, startDestination = AuthScreen.Login.route) {
        composable(AuthScreen.Login.route) { LoginScreen(authState.value, navController, authViewModel::login) }
        composable(AuthScreen.Signup.route) { SignupScreen(authState.value, navController, authViewModel::signup) }
        composable("home/{uid}") { backStackEntry ->
            val uid = backStackEntry.arguments?.getString("uid") ?: ""
            HomeScreen(navController, profileViewModel::loadUser, authViewModel::logout, uid)
        }
        composable("profile") { ProfileScreen(userState, navController = navController, profileViewModel::updateUserProfile) }
    }
}