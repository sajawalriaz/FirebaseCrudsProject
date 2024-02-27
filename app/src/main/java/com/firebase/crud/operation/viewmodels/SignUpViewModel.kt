package com.firebase.crud.operation.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class SignUpViewModel : ViewModel() {
    var email: String = ""
    var password: String = ""
    var confirmPassword: String = ""

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    private val _signUpResult = MutableLiveData<SignUpResult>()
    val signUpResult: LiveData<SignUpResult> = _signUpResult

    fun signUp(email: String, password: String, confirmPassword: String) {
        if (!isPasswordSecure(password, _signUpResult, confirmPassword, email)) {
            return
        }


        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _signUpResult.value = SignUpResult.Success(auth.currentUser)
                } else {
                    val errorMessage = task.exception?.message ?: "Unknown error"
                    Log.d("SignUpViewModel", "Error creating user account: $errorMessage")
                    _signUpResult.value = SignUpResult.Failure(errorMessage)
                }
            }
    }
}

sealed class SignUpResult {
    data class Success(val user: FirebaseUser?) : SignUpResult()
    data class Failure(val error: String) : SignUpResult()
}

fun isPasswordSecure(
    password: String,
    _signUpResult: MutableLiveData<SignUpResult>,
    confirmPassword: String,
    email: String
): Boolean {
    if (email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
        _signUpResult.value = SignUpResult.Failure("Please fill in all fields")
        return false
    }

    if (password != confirmPassword) {
        _signUpResult.value = SignUpResult.Failure("Passwords do not match")
        return false
    }
    if (password.length < 8) {
        _signUpResult.value = SignUpResult.Failure("Password must be at least six characters long")

        return false
    }

    // Check for complexity requirements (e.g., at least one uppercase letter, one lowercase letter, one digit, and one special character)
    val uppercasePattern = Regex("[A-Z]")
    val lowercasePattern = Regex("[a-z]")
    val digitPattern = Regex("\\d")
    val specialCharacterPattern = Regex("[^A-Za-z0-9]")

    if (!uppercasePattern.containsMatchIn(password) ||
        !lowercasePattern.containsMatchIn(password) ||
        !digitPattern.containsMatchIn(password) ||
        !specialCharacterPattern.containsMatchIn(password)
    ) {
        _signUpResult.value = SignUpResult.Failure("Password does not meet security requirements")
        return false

    }

    // Check against common passwords (optional)
    val commonPasswords = listOf("password", "12345678", "qwerty", "123456789")
    if (commonPasswords.contains(password.toLowerCase())) {
        return false
    }


    return true
}