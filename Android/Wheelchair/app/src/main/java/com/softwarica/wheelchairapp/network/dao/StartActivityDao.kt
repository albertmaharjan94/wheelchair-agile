package com.softwarica.wheelchairapp.network.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.softwarica.wheelchairapp.network.model.StartActivity
import com.softwarica.wheelchairapp.network.model.User

@Dao
interface StartActivityDao {
    @Insert
    suspend fun addStartActivity(startActivity: StartActivity)

    @Query("select * from StartActivity")
    suspend fun selectStartActivity(): Array<StartActivity>

    @Delete
    suspend fun deleteStartActivity(startActivity: StartActivity)
}