package com.softwarica.wheelchairapp.network.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.softwarica.wheelchairapp.network.model.User

@Dao
interface AuthDao {

    @Insert
    suspend fun addUser(user: User)

    @Query("select * from User where email = (:email) and password = (:password)")
    suspend fun checkAuth(email:String, password:String):User

}