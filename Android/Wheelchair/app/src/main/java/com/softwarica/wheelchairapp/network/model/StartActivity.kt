package com.softwarica.wheelchairapp.network.model

data class StartActivity(
    val vehicle : String?,
//    val date : String?,
//    val start_time :String,
    val activity : Array<StartTime>
)