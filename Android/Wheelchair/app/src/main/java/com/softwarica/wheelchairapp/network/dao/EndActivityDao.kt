package com.softwarica.wheelchairapp.network.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.softwarica.wheelchairapp.network.model.EndActivity

@Dao
interface EndActivityDao {
    @Insert
    suspend fun addEndActivity(endActivity: EndActivity)

    @Query("select * from EndActivity")
    suspend fun selectEndActivity(): Array<EndActivity>

    @Delete
    suspend fun deleteEndActivity(endActivity: EndActivity)

}