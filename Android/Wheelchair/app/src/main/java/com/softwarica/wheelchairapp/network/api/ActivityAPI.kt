package com.softwarica.wheelchairapp.network.api
import com.softwarica.wheelchairapp.network.model.EndActivity
import com.softwarica.wheelchairapp.network.model.StartActivity
import com.softwarica.wheelchairapp.network.response.ActivityResponse
import retrofit2.Response
import retrofit2.http.*


interface ActivityAPI {

    //Create Activity
//    @FormUrlEncoded
    @POST("activity")
    suspend fun startActivity(
        @Header("Authorization") token:String,
//        @Field("vehicle") vehicle : String,
        @Body activity: StartActivity
    ): Response<ActivityResponse>

    //Create Activity
    @POST("activity")
    suspend fun endActivity(
        @Header("Authorization") token:String,
        @Body activity: EndActivity
    ): Response<ActivityResponse>


    @GET("activity/{_id}")
    suspend fun getActivity(
        @Path("_id") _id: String
    ): Response<ActivityResponse>

}