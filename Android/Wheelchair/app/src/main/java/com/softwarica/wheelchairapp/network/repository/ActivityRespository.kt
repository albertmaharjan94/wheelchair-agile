package com.softwarica.wheelchairapp.network.repository

 import com.softwarica.wheelchairapp.network.api.ActivityAPI
 import com.softwarica.wheelchairapp.network.api.ServiceBuilder
 import com.softwarica.wheelchairapp.network.api.VehicleAPIRequest
 import com.softwarica.wheelchairapp.network.model.EndActivity
 import com.softwarica.wheelchairapp.network.model.StartActivity
 import com.softwarica.wheelchairapp.network.model.StartTime
 import com.softwarica.wheelchairapp.network.response.ActivityResponse


class ActivityRespository : VehicleAPIRequest() {
    private val activityAPI = ServiceBuilder.buildService(ActivityAPI::class.java)


    //Add Activity
    suspend fun startActivity(vehicle: String, start_time:String): ActivityResponse  {

        val response =  apiRequest{
            activityAPI.startActivity(
                ServiceBuilder.token!!,
                StartActivity(vehicle,arrayOf(StartTime(start_time)))
            )
        }
        return if (response.success == true || response.success == false){
            response
        } else{
            startActivity(vehicle,start_time)
        }
    }

    suspend fun endActivity(endActivity: EndActivity): ActivityResponse  {
        val response =  apiRequest{
            activityAPI.endActivity(
                ServiceBuilder.token!!,
                endActivity
            )
        }
        return if (response.success == true || response.success == false){
            response
        } else{
            endActivity(endActivity)
        }
    }

//    suspend fun getActivity(): StartActivity? {
//        val response =  apiRequest{
//            activityAPI.getActivity(
//                ServiceBuilder.logged_user?.userid!!
//            )
//        }
//        return if (response.success == true || response.success == false){
//            response.activity
//        } else{
//            getActivity()
//        }
//    }

}