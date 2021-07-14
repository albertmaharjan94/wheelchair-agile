package com.softwarica.wheelchairapp.network.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class StartActivity(
    val vehicle: String?,
//    val date : String?,
//    val start_time :String,
    val activity: Array<StartTime>
) {
    @PrimaryKey(autoGenerate = true)
    var activityId: Int = 0
}


