package com.softwarica.wheelchairapp.network.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Tracker(
    val user: String,
    val vehicle: String,
    val location: Coordinates
) {
    @PrimaryKey(autoGenerate = true)
    var _Id: Int = 0
}
