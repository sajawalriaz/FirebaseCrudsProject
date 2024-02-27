package com.firebase.crud.operation.models

import com.google.firebase.database.Exclude
import java.io.Serializable

data class User(
    var name: String,
    var id: String,
    var cnic: String,
    var fatherName: String,
    var time: String
):Serializable{
    @Exclude
    fun toMap(): Map<String, Any> {
        val map = mutableMapOf<String, Any>()
        map["id"] = id
        map["name"] = name
        map["cnic"] = cnic
        map["fatherName"] = fatherName
        return map
    }
}