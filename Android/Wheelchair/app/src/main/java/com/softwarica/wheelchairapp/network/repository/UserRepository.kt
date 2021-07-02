package com.softwarica.wheelchairapp.network.repository

import android.util.Log
import com.softwarica.wheelchairapp.network.api.AuthAPI
import com.softwarica.wheelchairapp.network.api.ServiceBuilder
import com.softwarica.wheelchairapp.network.api.VehicleAPIRequest

import com.softwarica.wheelchairapp.network.model.User
import kotlin.math.log


class UserRepository : VehicleAPIRequest() {

    //create retrofit instance of user API
    private val authAPI = ServiceBuilder.buildService(AuthAPI::class.java)


    //Login user
    suspend fun checkUser(email: String, password: String): User?{
        val response =  apiRequest{
            authAPI.loginUser(email, password)
        }
        ServiceBuilder.logged_user = response.data
        ServiceBuilder.token = response.data?.token

//        return if (response.success == true || response.success == false){
            Log.d("Logingg", response.toString())
            return response.data

//        } else{
//            checkUser(email,password)
//        }
    }

    suspend fun getProfile(){
        Log.d("LoggedUser", ServiceBuilder.logged_user.toString() )
        val response = apiRequest {
            authAPI.getProfile(ServiceBuilder.token!!)
        }
        ServiceBuilder.logged_user = response.data
    }

}