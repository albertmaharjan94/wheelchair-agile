package com.softwarica.wheelchairapp.network.api
import com.softwarica.wheelchairapp.network.model.Activity
import com.softwarica.wheelchairapp.network.response.ActivityResponse
import retrofit2.Response
import retrofit2.http.*


interface ActivityAPI {

    //Create Activity
    @POST("activity")
    suspend fun activity(
        @Header("Authorization") token:String,
        @Body activity: Activity
    ): Response<ActivityResponse>


    @GET("activity/{_id}")
    suspend fun getActivity(
        @Path("_id") _id: String
    ): Response<ActivityResponse>

}