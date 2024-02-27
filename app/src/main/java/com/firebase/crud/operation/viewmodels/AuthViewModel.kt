package com.firebase.crud.operation.viewmodels

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.firebase.crud.operation.activities.ForgotPasswordFragment
import com.firebase.crud.operations.R
import com.google.firebase.auth.FirebaseAuth

class AuthViewModel:ViewModel() {
    var email: String = ""
    var password: String = ""
        get() = field
        set(value) { field = value }

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val _isLoggedIn = MutableLiveData<Boolean>()
    val isLoggedIn: LiveData<Boolean>
        get() = _isLoggedIn

    fun loginWithEmail(email: String, password: String) {
        if (email.isEmpty() || password.isEmpty()) {
            _isLoggedIn.value = false
            return
        }

        firebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    _isLoggedIn.value = true

                } else {
                    _isLoggedIn.value = false
                }
            }
    }

}