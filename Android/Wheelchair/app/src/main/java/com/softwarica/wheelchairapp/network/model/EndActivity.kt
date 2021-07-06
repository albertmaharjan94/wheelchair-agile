package com.softwarica.wheelchairapp.network.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class EndActivity(
    @PrimaryKey
    val start_time: String,
    val vehicle: String?,
    val end_time: String?,
    val speed: Int?,
    val kilometer: Int?
)