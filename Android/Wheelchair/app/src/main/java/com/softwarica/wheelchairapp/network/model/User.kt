package com.softwarica.wheelchairapp.network.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val name: String?,
    val userid: String?,
    val email: String?,
    var password : String?,
    val address: String?,
    val contact: String?,
    var token : String?,
    val vehicle : String?,
    val distance: Float?,
    val speed: Float?,
){
    @PrimaryKey(autoGenerate = true)
    var _Id: Int = 0
}