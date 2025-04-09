package com.example.userhub.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.userhub.UserHubApplication
import com.example.userhub.auth.FirebaseAuthRepository
import com.example.userhub.data.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: FirebaseAuthRepository) : ViewModel() {
    private val _user = MutableStateFlow<User?>(null)
    val user: StateFlow<User?> = _user

    fun loadUser(uid: String) {
        viewModelScope.launch {
            _user.value = repository.getUserProfile(uid)
        }
    }

    fun updateUserProfile(newName: String) {
        viewModelScope.launch {
            _user.value?.let { user ->
                val updatedUser = user.copy(displayName = newName)
                repository.updateUserProfile(updatedUser)
                _user.value = updatedUser
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as UserHubApplication)
                ProfileViewModel(repository = application.container.userHubRepository)
            }
        }
    }
}
