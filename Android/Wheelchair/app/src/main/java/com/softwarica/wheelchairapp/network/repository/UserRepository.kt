package com.softwarica.wheelchairapp.network.repository

import android.util.Log
import com.softwarica.wheelchairapp.network.api.AuthAPI
import com.softwarica.wheelchairapp.network.api.ServiceBuilder
import com.softwarica.wheelchairapp.network.api.VehicleAPIRequest
import com.softwarica.wheelchairapp.network.dao.AuthDao
import com.softwarica.wheelchairapp.network.database_conf.WheelDB

import com.softwarica.wheelchairapp.network.model.User
import com.softwarica.wheelchairapp.network.response.UserResponse
import java.lang.Exception
import kotlin.math.log


class UserRepository(private val authDao: AuthDao) : VehicleAPIRequest() {

    //create retrofit instance of user API
    private val authAPI = ServiceBuilder.buildService(AuthAPI::class.java)


    //Login user
    suspend fun checkUser(email: String, password: String): User {
        var data = try {
            val response = apiRequest {
                authAPI.loginUser(email, password)
            }
            response.data
        }
        catch (ex: Exception){
            ex.printStackTrace()
            null
        }

        if (data == null) data = authDao.checkAuth(email, password)

        ServiceBuilder.logged_user = data
        ServiceBuilder.token = data.token

        return data
    }


    suspend fun getProfile(password: String) {
        Log.d("LoggedUser", ServiceBuilder.logged_user.toString())
        val data = try {
            val response = apiRequest {
                authAPI.getProfile(ServiceBuilder.token!!)
            }
            response.data
        }
        catch (ex: Exception){
            null
        }

        if(data != null){
            data.password = password
            data.token = ServiceBuilder.token
            ServiceBuilder.logged_user = data
            authDao.addUser(data)
        }
    }

}