package com.softwarica.wheelchairapp.network.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkRequest
import android.widget.Toast
import com.softwarica.wheelchairapp.Utils.Variables

class ConnectivityReceiver(val context: Context) {

    fun registerNetworkCallback() {
        try {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val builder = NetworkRequest.Builder()
            connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    Variables().isNetworkConnected = true // Global Static Variable
                    Toast.makeText(context, "Internet Connected", Toast.LENGTH_SHORT).show()
                }

                override fun onLost(network: Network) {
                    Variables().isNetworkConnected = false // Global Static Variable
                    Toast.makeText(context, "Internet Connection lost", Toast.LENGTH_SHORT).show()
                }
            }
            )
            Variables().isNetworkConnected = false
        } catch (e: Exception) {
            Variables().isNetworkConnected = false
        }
    }

}