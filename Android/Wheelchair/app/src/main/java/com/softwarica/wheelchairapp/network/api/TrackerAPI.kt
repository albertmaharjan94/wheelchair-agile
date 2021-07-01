package com.softwarica.wheelchairapp.network.api

import com.softwarica.wheelchairapp.network.model.Tracker
import com.softwarica.wheelchairapp.network.response.Res
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface TrackerAPI {

    @POST("tracker")
    suspend fun tracker(
        @Header("Authorization") token:String,
        @Body tracker: Tracker
    ): Response<Res>

}