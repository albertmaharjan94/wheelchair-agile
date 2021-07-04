package com.softwarica.wheelchairapp.network.model

import androidx.room.Entity

//@Entity
data class Tracker(
    val user: String,
    val vehicle : String,
    val location : Coordinates
)
