package com.example.userhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.userhub.navigation.AuthNavGraph
import com.example.userhub.ui.theme.UserHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UserHubTheme {

                Surface (
                    modifier = Modifier
                        .statusBarsPadding()
                        .navigationBarsPadding()
                ){
                    val navController = rememberNavController()
                    AuthNavGraph(navController)
                }

            }
        }
    }
}