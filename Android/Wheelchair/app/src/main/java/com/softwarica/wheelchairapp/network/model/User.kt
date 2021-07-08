package com.softwarica.wheelchairapp.network.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey()
    val userid: String,
    val name: String?,
    val email: String?,
    var password : String?,
    val address: String?,
    val contact: String?,
    val emContact: String?,
    var token : String?,
    val vehicle : String?,
    val distance: Float?,
    val speed: Float?,
)