package com.example.userhub.auth

import android.util.Log
import com.example.userhub.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirebaseAuthRepository {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val usersCollection = FirebaseFirestore.getInstance().collection("users")

    fun getCurrentUserUid(): String? = auth.currentUser?.uid

    suspend fun login(email: String, password: String): Res {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            Res.Success(user?.uid)
        } catch (e: FirebaseAuthException) {
            Res.Error(getFirebaseAuthErrorMessage(e))
        }
    }

    suspend fun signup(email: String, password: String, displayName: String): Res {
        return try {
            val user = auth.createUserWithEmailAndPassword(email, password).await().user
            user?.let {
                val newUser = User(uid = it.uid, email = it.email ?: "", displayName = displayName)
                usersCollection.document(it.uid).set(newUser, SetOptions.merge()).await()
            }
            Res.Success(user?.uid)
        } catch (e: FirebaseAuthException) {
            Log.d("Error", e.errorCode)
            Res.Error(getFirebaseAuthErrorMessage(e))
        }
    }

    suspend fun getUserProfile(uid: String): User? {
        return try {
            val snapshot = usersCollection.document(uid).get().await()
            snapshot.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun updateUserProfile(user: User) {
        usersCollection.document(user.uid).set(user).await()
    }

    suspend fun logout() = auth.signOut()

    private fun getFirebaseAuthErrorMessage(e: FirebaseAuthException): String {
        return when (e.errorCode) {
            "ERROR_WEAK_PASSWORD" -> "Password should be at least 6 characters"
            "ERROR_INVALID_EMAIL" -> "Invalid email format."
            "ERROR_WRONG_PASSWORD" -> "Incorrect password. Please try again."
            "ERROR_USER_NOT_FOUND" -> "No account found with this email."
            "ERROR_USER_DISABLED" -> "This account has been disabled."
            "ERROR_TOO_MANY_REQUESTS" -> "Too many failed attempts. Try again later."
            "ERROR_INVALID_CREDENTIAL" -> "Invalid email or password. Please check your credentials."
            "ERROR_OPERATION_NOT_ALLOWED" -> "Login with email/password is not allowed."
            "ERROR_NETWORK_REQUEST_FAILED" -> "Network error. Please check your connection."
            else -> "Authentication failed. Please try again."
        }
    }
}

sealed class Res {
    data class Success(val uid: String?): Res()
    data class Error(val message: String) : Res()
}

