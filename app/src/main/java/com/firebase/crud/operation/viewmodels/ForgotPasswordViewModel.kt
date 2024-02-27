package com.firebase.crud.operation.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordViewModel : ViewModel(){
    var email: String = ""
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val isForgetPass = MutableLiveData<Boolean>()

    init {
        isForgetPass.value = false
    }
    fun resetPassword() {
        if (email.isEmpty())
            return
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("SignUpViewModel", "Error creating user account: 144")
                    isForgetPass.value=true
                    // Password reset email sent successfully
                } else {
                    isForgetPass.value=false
                    // Handle failure
                    val errorMessage = task.exception?.message ?: "Unknown error"

                    Log.d("SignUpViewModel", "Error creating user account: $errorMessage")

                }
            }
    }
}