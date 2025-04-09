package com.example.userhub.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.userhub.UserHubApplication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: FirebaseAuthRepository) : ViewModel() {
    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result: Res = repository.login(email, password)
            _authState.value =
                if (result is Res.Success) AuthState.Success(result.uid) else AuthState.Error((result as Res.Error).message)
        }
    }

    fun signup(email: String, password: String, name: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.signup(email, password, name)
            _authState.value =
                if (result is Res.Success) AuthState.Success(result.uid) else AuthState.Error((result as Res.Error).message)
        }
    }

    fun logout() {
        viewModelScope.launch {
            repository.logout()
            _authState.value = AuthState.Idle
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as UserHubApplication)
                AuthViewModel(repository = application.container.userHubRepository)
            }
        }
    }
}

sealed class AuthState {
    data object Idle : AuthState()
    data object Loading : AuthState()
    data class Success(val uid: String?) : AuthState()
    data class Error(val message: String) : AuthState()
}
