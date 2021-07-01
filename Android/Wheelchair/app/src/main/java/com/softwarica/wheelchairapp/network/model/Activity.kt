package com.softwarica.wheelchairapp.network.model

data class Activity(
    val vehicle : String,
    val date : String,
    val start_time :String,
    val end_time : String,
    val avg_speed: Int,
    val kilometer: Int
)