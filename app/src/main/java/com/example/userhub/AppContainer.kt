package com.example.userhub

import com.example.userhub.auth.FirebaseAuthRepository

interface AppContainer{
    val userHubRepository: FirebaseAuthRepository
}

class DefaultAppContainer(): AppContainer {
    override val userHubRepository: FirebaseAuthRepository by lazy {
        FirebaseAuthRepository()
    }
}