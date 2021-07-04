package com.softwarica.wheelchairapp.network.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.softwarica.wheelchairapp.network.model.EndActivity
import com.softwarica.wheelchairapp.network.model.StartActivity
import com.softwarica.wheelchairapp.network.model.User

@Dao
interface ActivityDao {
    @Insert
    suspend fun addStartActivity(startActivity: StartActivity)

//    @Insert
//    suspend fun addEndActivity(endActivity: EndActivity)
//
//    @Query("select * from StartActivity")
//    suspend fun selectStartActivity(): User
//
//    @Query("select * from EndActivity")
//    suspend fun selectEndActivity() : Array<EndActivity>
//
//    @Delete
//    suspend fun deleteStartActivity(startActivity: StartActivity)
//
//    @Delete
//    suspend fun deleteEndActivity(endActivity: EndActivity)
}