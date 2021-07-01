package com.softwarica.wheelchairapp.network.repository


 import com.softwarica.wheelchairapp.network.api.ActivityAPI
 import com.softwarica.wheelchairapp.network.api.ServiceBuilder
 import com.softwarica.wheelchairapp.network.api.VehicleAPIRequest
 import com.softwarica.wheelchairapp.network.model.Activity
 import com.softwarica.wheelchairapp.network.response.ActivityResponse


class ActivityRespository : VehicleAPIRequest() {
    private val activityAPI = ServiceBuilder.buildService(ActivityAPI::class.java)


    //Add Activity
    suspend fun addActivity(activity: Activity): ActivityResponse  {
        val response =  apiRequest{
            activityAPI.activity(
                ServiceBuilder.token!!,
                activity
            )
        }
        return if (response.success == true || response.success == false){
            response
        } else{
            addActivity(activity)
        }
    }

    suspend fun getActivity(): Activity? {
        val response =  apiRequest{
            activityAPI.getActivity(
                ServiceBuilder.logged_user?.userid!!
            )
        }
        return if (response.success == true || response.success == false){
            response.activity
        } else{
            getActivity()
        }
    }

}