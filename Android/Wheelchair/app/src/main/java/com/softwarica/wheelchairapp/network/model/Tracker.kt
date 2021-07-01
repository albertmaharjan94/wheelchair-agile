package com.softwarica.wheelchairapp.network.model

data class Tracker(
    val user: String,
    val vehicle : String,
    val location : Coordinates
)