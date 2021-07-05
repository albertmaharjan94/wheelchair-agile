package com.softwarica.wheelchairapp.network.model

import androidx.room.TypeConverter
import com.google.gson.Gson

class StartTimeConverter {

    @TypeConverter
    fun arrayToJson(value: Array<StartTime>?) = Gson().toJson(value)

    @TypeConverter
    fun jsonToArray(value: String) = Gson().fromJson(value, Array<StartTime>::class.java)

}