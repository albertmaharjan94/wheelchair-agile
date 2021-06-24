package com.softwarica.wheelchairapp.ui.main.Maps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.softwarica.wheelchairapp.R
import java.io.IOException
import java.util.*


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MapsFragment : Fragment() {
    var locationManager: LocationManager? = null
    var mapFragment: SupportMapFragment? = null
    var current_location: LatLng? = null
    var current_address: String? = null
    var locationListener: LocationListener = MyLocationListener()

    var mCurrLocationMarker: Marker? = null
    var markerOptions: MarkerOptions = MarkerOptions()
    private lateinit var mMap: GoogleMap

    var mGoogleMap: GoogleMap? = null
    var markerName: Marker? = null


    private val callback = OnMapReadyCallback { googleMap ->
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
            if (markerName == null) {
                markerName = googleMap.addMarker(MarkerOptions().position(
                    current_location
                ).title(current_address))
            } else {
                markerName!!.position = current_location
                markerName!!.title = current_address
            }
            googleMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(current_location, 13F), 100, null
            )
            googleMap.uiSettings.isZoomControlsEnabled = true
            googleMap.uiSettings.isCompassEnabled = true
            googleMap.uiSettings.isMapToolbarEnabled = true
//
//            markerOptions.position(current_location)
//            markerOptions.title(current_address)
//            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA))
//            mCurrLocationMarker = googleMap.addMarker(markerOptions)
//
//            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(current_location, 17f))

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        locationManager =
            requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        try {
            locationManager!!.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 1000, 1F, locationListener
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }


        mapFragment?.getMapAsync(callback)
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
        override fun onLocationChanged(loc: Location) {
            current_location = LatLng(loc.latitude, loc.longitude)

            /*------- To get city name from coordinates -------- */
            val gcd = Geocoder(context, Locale.getDefault())
            val addresses: List<Address>
            try {
                addresses = gcd.getFromLocation(
                    loc.latitude,
                    loc.longitude, 1
                )
                if (addresses.isNotEmpty()) {
                    println(addresses[0].getLocality())
                    current_address = addresses[0].locality
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val s = """
            My Current City is: $current_address
            """.trimIndent()
            Log.d("CUrrent", s)
            mapFragment?.getMapAsync(callback)
        }

        override fun onProviderDisabled(provider: String) {}
        override fun onProviderEnabled(provider: String) {}
        override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    }
}