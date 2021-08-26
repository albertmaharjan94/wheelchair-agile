
package com.softwarica.wheelchairapp.network.api
import com.softwarica.wheelchairapp.network.response.UserResponse
import retrofit2.Response
import retrofit2.http.*

interface AuthAPI {

    //User Login
    @FormUrlEncoded
    @POST("auth/login")
    suspend fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Response<UserResponse>

    @GET("auth/profile")
    suspend fun getProfile(
        @Header("Authorization") token:String,
    ): Response<UserResponse>
}