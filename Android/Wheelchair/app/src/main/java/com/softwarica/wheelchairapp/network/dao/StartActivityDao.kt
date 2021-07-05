package com.softwarica.wheelchairapp.network.dao

import androidx.room.Dao
import androidx.room.Insert
import com.softwarica.wheelchairapp.network.model.StartActivity

@Dao
interface StartActivityDao {
    @Insert
    suspend fun addStartActivity(startActivity: StartActivity)


//    @Query("select * from StartActivity")
//    suspend fun selectStartActivity(): User
//
//    @Delete
//    suspend fun deleteStartActivity(startActivity: StartActivity)
//

}