package com.softwarica.wheelchairapp.ui.main.Maps

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.ProgressDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.softwarica.wheelchairapp.R
import com.softwarica.wheelchairapp.network.api.ServiceBuilder
import com.softwarica.wheelchairapp.network.database_conf.WheelDB
import com.softwarica.wheelchairapp.network.model.Coordinates
import com.softwarica.wheelchairapp.network.model.Tracker
import com.softwarica.wheelchairapp.network.repository.TrackerRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapsFragment : Fragment() {
    var locationManager: LocationManager? = null
    var current_location: LatLng? = null
    var current_address: String? = null
    var changeLoc : Boolean ?= true
    var locationListener: LocationListener = MyLocationListener()

    var mCurrLocationMarker: Marker? = null
    var mapView: MapView? = null
    var markerName: Marker? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var btnEmergency: Button? = null
    private var smsManager: SmsManager? = null

    var first_marking = true
    private var googleMap: GoogleMap? = null

    val emergency_number = "+9779808438993"

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { mMap ->
        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        try {
            googleMap = mMap

            // For showing a move to my location button
            googleMap!!.isMyLocationEnabled = true

            if (markerName == null) {
                markerName = googleMap!!.addMarker(
                    MarkerOptions().position(
                        current_location
                    ).title(current_address)
                )
            } else {
                markerName!!.position = current_location
                markerName!!.title = current_address

            }
            if (first_marking) {
                // For zooming automatically to the location of the marker
                val cameraPosition =
                    CameraPosition.Builder().target(current_location).zoom(12f).build()
                googleMap!!.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                first_marking = false
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Handler(Looper.getMainLooper()).postDelayed({
            changeLoc = true
        }, 1000)
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onPause() {
        super.onPause()
        try {
            requireActivity().unregisterReceiver(sendBroadcastReceiver)
            requireActivity().unregisterReceiver(deliveryBroadcastReciever)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            requireActivity().unregisterReceiver(sendBroadcastReceiver)
            requireActivity().unregisterReceiver(deliveryBroadcastReciever)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("MissingPermission")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestMapPermission()

        btnEmergency = view.findViewById(R.id.btnEmergency)
        mapView = view.findViewById(R.id.mapView) as MapView
        mapView!!.onCreate(savedInstanceState)
        mapView!!.onResume()
        smsManager = SmsManager.getDefault()
        btnEmergency!!.setOnClickListener {
            AlertDialog.Builder(requireActivity())
                .setTitle("Send Emergency Message")
                .setMessage("Send your location to emergency contact?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton("Send") { _, _ ->
                    if (current_location != null ||
                        requestMapPermission()
                    ) {
                        try {
                            fusedLocationClient.lastLocation
                                .addOnSuccessListener { loc: Location? ->
                                    Log.d("location", loc.toString())
                                    if (loc != null) {
                                        current_location = LatLng(loc.latitude, loc.longitude)
                                        current_address = getLocationAddress(loc)
                                        mapView!!.getMapAsync(callback)
                                    }
                                }
                        } catch (e: java.lang.Exception) {
                            e.printStackTrace()
                        }
                        sendSMS(
                            emergency_number,
                            "!!!EMERGENCY!!!\n" +
                                    "Latitude: ${current_location!!.latitude} , Longitude: ${current_location!!.longitude}\n" +
                                    "Address: ${current_address}\n" +
                                    "http://www.google.com/maps/place/${current_location!!.latitude},${current_location!!.longitude}"
                        )
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Required permission not found. Please try again.",
                            Toast.LENGTH_LONG
                        ).show()


                    }

                }
                .setNegativeButton("Cancel", null).show()
        }


        try {
            MapsInitializer.initialize(requireActivity().applicationContext)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }


        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        fusedLocationClient.lastLocation
            .addOnSuccessListener { loc: Location? ->
                Log.d("location", loc.toString())
                if (loc != null) {
                    current_location = LatLng(loc.latitude, loc.longitude)
                    current_address = getLocationAddress(loc)
                    mapView!!.getMapAsync(callback)
                }
            }

        try {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 1F, locationListener
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }


    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): MapsFragment {
            return MapsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    private inner class MyLocationListener : LocationListener {
        @SuppressLint("MissingPermission")
        override fun onLocationChanged(loc: Location) {
            // tracker function here

            current_address = getLocationAddress(loc)
            val coordinates = arrayOf<Double>(
                loc.latitude, loc.longitude
            )
//            Toast.makeText(context, "location.........", Toast.LENGTH_SHORT).show()
            if(changeLoc!!){
                changeLoc = false
                tracker(coordinates)
            }

            mapView!!.getMapAsync(callback)

        }

        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }

    private fun tracker(coordinates: Array<Double>) {
        val getTrackerInstance = WheelDB.getinstance(requireContext()).getTrackerDao()
        val userDetail = ServiceBuilder.logged_user
        val tracker = Tracker(
            userDetail?.userid!!,
            userDetail.vehicle!!,
            Coordinates(coordinates)
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = TrackerRepository().addTracker(tracker)
                if (response?.success!!) {
                    withContext(Dispatchers.Main) {
                        getTrackerInstance.addTracker(tracker)
//                        Toast.makeText(
//                            context,
//                            "Location updated",
//                            Toast.LENGTH_LONG
//                        )
//                            .show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(
                            context,
                            "Location failed to update",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, ex.toString(), Toast.LENGTH_LONG).show()
                }
            }

        }
    }

    private fun getLocationAddress(loc: Location): String {
        val gcd = Geocoder(context, Locale.getDefault())
        var _current_address = ""
        val addresses: List<Address>
        try {
            addresses = gcd.getFromLocation(
                loc.latitude,
                loc.longitude, 1
            )
            if (addresses.isNotEmpty()) {
                println(addresses[0].locality)
                _current_address = addresses[0].locality
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return _current_address
    }

    //     sms
    var sendBroadcastReceiver: BroadcastReceiver? = SentReceiver()
    var deliveryBroadcastReciever: BroadcastReceiver = DeliverReceiver()

    internal class DeliverReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, arg1: Intent) {
            when (resultCode) {
                Activity.RESULT_OK -> Toast.makeText(
                    context, "Message Delivered",
                    Toast.LENGTH_SHORT
                ).show()
                Activity.RESULT_CANCELED -> Toast.makeText(
                    context, "Message Not Delivered",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendSMS(phoneNumber: String, message: String) {
        val SENT = "SMS_SENT"
        val DELIVERED = "SMS_DELIVERED"
        val sentPI = PendingIntent.getBroadcast(
            context, 0, Intent(
                SENT
            ), 0
        )
        val deliveredPI = PendingIntent.getBroadcast(
            context, 0,
            Intent(DELIVERED), 0
        )
        requireActivity().registerReceiver(sendBroadcastReceiver, IntentFilter(SENT))
        requireActivity().registerReceiver(deliveryBroadcastReciever, IntentFilter(DELIVERED))
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI)

    }

    internal class SentReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, arg1: Intent) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    Toast.makeText(context, "Message Sent", Toast.LENGTH_SHORT)
                        .show()
                }
                SmsManager.RESULT_ERROR_GENERIC_FAILURE -> Toast.makeText(
                    context, "Generic failure",
                    Toast.LENGTH_SHORT
                ).show()
                SmsManager.RESULT_ERROR_NO_SERVICE -> Toast.makeText(
                    context, "No service",
                    Toast.LENGTH_SHORT
                ).show()
                SmsManager.RESULT_ERROR_NULL_PDU -> Toast.makeText(
                    context,
                    "Null PDU",
                    Toast.LENGTH_SHORT
                )
                    .show()
                SmsManager.RESULT_ERROR_RADIO_OFF -> Toast.makeText(
                    context, "Radio off",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun requestMapPermission(): Boolean {
        val send_sms = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.SEND_SMS
        )
        val read_sms = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.READ_SMS
        )
        val cor_loc = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val fine_loc = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (send_sms != PackageManager.PERMISSION_GRANTED
            || read_sms != PackageManager.PERMISSION_GRANTED
            || cor_loc != PackageManager.PERMISSION_GRANTED
            || fine_loc != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.READ_SMS,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                642
            )
        } else {
            Log.d("DISCOVERING-PERMISSIONS", "Permissions Granted")
        }

        return requireContext().checkCallingOrSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED
                && requireContext().checkCallingOrSelfPermission(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                && requireContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && requireContext().checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}