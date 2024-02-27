package com.firebase.crud.operation.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.firebase.crud.operation.models.User

class DashboardViewModel : ViewModel() {
    var name: String = ""
    var id: String = ""
    var cnic: String = ""
    var fatherName: String = ""


    val userData = MutableLiveData<User>()

    // Define LiveData to handle navigation
    private val _navigateToNextFragment = MutableLiveData<User?>()
    val navigateToNextFragment get() = _navigateToNextFragment

    fun uploadData(name: String, id: String, cnic: String, fatherName: String, time: String) {
        this.name = name
        this.id = id
        this.cnic = cnic
        this.fatherName = fatherName
        userData.value = User(name, id, cnic, fatherName,time)


    }


    // Function to handle item click
    fun onItemClick(user: User) {
        // Set the value of navigateToNextFragment LiveData
        _navigateToNextFragment.value = user
    }

    fun deleteUser(user: User) {
        // Set the value of navigateToNextFragment LiveData
        Log.d("DashboardViewModel", "Deleting user: ${user.name}")

        _navigateToNextFragment.value = user
    }

    // This function should be called when navigation is done
    fun onNavigationComplete() {
        // Reset the value of navigateToNextFragment LiveData
        _navigateToNextFragment.value = null
    }

}