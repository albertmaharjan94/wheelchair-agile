package com.softwarica.wheelchairapp.network.api

import android.content.Context
import android.widget.Toast
import com.softwarica.wheelchairapp.network.database_conf.WheelDB
import com.softwarica.wheelchairapp.network.repository.ActivityRespository
import com.softwarica.wheelchairapp.network.repository.TrackerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DaoToAPI() {
    fun proceedStartActivity(context: Context){
        val getStartActivityInstance = WheelDB.getinstance(context).getStartActivityDao()
        val getEndActivityInstance = WheelDB.getinstance(context).getEndActivityDao()
        CoroutineScope(Dispatchers.IO).launch {
            val startArr = getStartActivityInstance.selectStartActivity()
            if (startArr.isNotEmpty()) {
                for (activity in startArr) {
                    val response = ActivityRespository().startActivity(
                        activity.vehicle!!,
                        activity.activity[0].start_time
                    )
                    if (response?.success == true) {
                        getStartActivityInstance.deleteStartActivity(activity)
                    }
                }
                withContext(Main) {
                    Toast.makeText(
                        context,
                        "StartActivity Data transferred to API",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

        CoroutineScope(Dispatchers.IO).launch {
            val endArr = getEndActivityInstance.selectEndActivity()
            if (endArr.isNotEmpty()){
                for(activity in endArr){
                    val response = ActivityRespository().endActivity(activity)
                    if (response?.success == true){
                        getEndActivityInstance.deleteEndActivity(activity)
                    }
                }
                withContext(Main){
                    Toast.makeText(context, "EndActivity Data transferred to API", Toast.LENGTH_SHORT).show()
                }
            }
//            withContext(Main){
//                Toast.makeText(context, "Activity Data transferred to API", Toast.LENGTH_SHORT).show()
//            }
        }
    }

//    fun proceedEndActivity(context: Context){
//        val getEndActivityInstance = WheelDB.getinstance(context).getEndActivityDao()
//        CoroutineScope(Dispatchers.IO).launch {
//            val endArr = getEndActivityInstance.selectEndActivity()
//            if (endArr.isNotEmpty()){
//                for(activity in endArr){
//                    val response = ActivityRespository().endActivity(activity)
//                    if (response?.success == true){
//                        getEndActivityInstance.deleteEndActivity(activity)
//                    }
//                }
//                withContext(Main){
//                    Toast.makeText(context, "EndActivity Data transferred to API", Toast.LENGTH_SHORT).show()
//                }
//            }
//            withContext(Main){
////                Toast.makeText(context, "No end activity data to update", Toast.LENGTH_SHORT).show()
//            }
//        }
//    }

    fun proceedTracker(context: Context){
        val getTrackerInstance = WheelDB.getinstance(context).getTrackerDao()
        CoroutineScope(Dispatchers.IO).launch {
            val trackerArr = getTrackerInstance.selectTracker()
            if(trackerArr.isNotEmpty()){
                for(activity in trackerArr){
                    val response = TrackerRepository().addTracker(activity)
                    if (response?.success == true){
                        getTrackerInstance.deleteTracker(activity)
                    }
                }
                withContext(Main){
                    Toast.makeText(context, "Tracker Data transferred to API", Toast.LENGTH_SHORT).show()
                }
            }
            withContext(Main){
//                Toast.makeText(context, "No data to update", Toast.LENGTH_SHORT).show()
            }
        }
    }
}