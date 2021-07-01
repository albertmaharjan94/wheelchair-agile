package com.softwarica.wheelchairapp.ui.main.Activity

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.softwarica.wheelchairapp.R
import com.softwarica.wheelchairapp.network.api.ServiceBuilder
import com.softwarica.wheelchairapp.network.model.Activity
import com.softwarica.wheelchairapp.network.model.Coordinates
import com.softwarica.wheelchairapp.network.model.Tracker
import com.softwarica.wheelchairapp.network.repository.ActivityRespository
import com.softwarica.wheelchairapp.network.repository.TrackerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class TrackActivity : AppCompatActivity() {

    private lateinit var etSpeed: EditText
    private lateinit var etDistance: EditText
    private lateinit var etLat: EditText
    private lateinit var etLon: EditText
    private lateinit var btnChange: Button
    private lateinit var btnChangeLocation: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)

        binding()

        val userDetail = ServiceBuilder.logged_user


        btnChange.setOnClickListener {
            val activity = Activity(
                userDetail?.vehicle!!,
                "2021-06-23",
                "2021-06-23",
                "2021-06-23",
                etSpeed.text.toString().toInt(),
                etDistance.text.toString().toInt(),
            )

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = ActivityRespository().addActivity(activity)
                    if (response.success!!) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(this@TrackActivity, "Tracker updated", Toast.LENGTH_LONG)
                                .show()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@TrackActivity,
                                "Tracker failed to update",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@TrackActivity, ex.toString(), Toast.LENGTH_LONG).show()
                    }
                }

            }
        }


        btnChangeLocation.setOnClickListener {
            val cordinates = arrayOf<Int>(
                etLat.text.toString().toInt(),
                etLon.text.toString().toInt()
            )
            val tracker = Tracker(
                userDetail?.userid!!,
                userDetail.vehicle!!,
                Coordinates(cordinates)
            )

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = TrackerRepository().addTracker(tracker)
                    if (response.success!!) {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@TrackActivity,
                                "Location updated",
                                Toast.LENGTH_LONG
                            )
                                .show()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast.makeText(
                                this@TrackActivity,
                                "Location failed to update",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (ex: Exception) {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@TrackActivity, ex.toString(), Toast.LENGTH_LONG).show()
                    }
                }

            }
        }


    }

    private fun binding() {

    }
}