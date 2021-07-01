package com.softwarica.wheelchairapp.network.repository


import com.softwarica.wheelchairapp.network.api.ServiceBuilder
import com.softwarica.wheelchairapp.network.api.TrackerAPI
import com.softwarica.wheelchairapp.network.api.VehicleAPIRequest
import com.softwarica.wheelchairapp.network.model.Tracker
import com.softwarica.wheelchairapp.network.response.Res


class TrackerRepository : VehicleAPIRequest(){

    private var trackerAPI = ServiceBuilder.buildService(TrackerAPI::class.java)

    //Add Activity
    suspend fun addTracker(tracker: Tracker): Res {
        val response =  apiRequest{
            trackerAPI.tracker(
                ServiceBuilder.token!!,
                tracker
            )
        }
        return if (response.success == true || response.success == false){
            response
        } else{
            addTracker(tracker)
        }
    }

//    suspend fun getLocation(): Activity? {
//        val response =  apiRequest{
//            trackerAPI.getLocation(
//                ServiceBuilder.logged_user?.userid!!
//            )
//        }
//        return if (response.success == true || response.success == false){
//            response.location
//        } else{
//            getLocation()
//        }
//    }
}