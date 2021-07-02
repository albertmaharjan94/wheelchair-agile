
package com.softwarica.wheelchairapp.network.api
import com.softwarica.wheelchairapp.Utils.Constants
import com.softwarica.wheelchairapp.network.model.User
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceBuilder {
//    private const val BaseURL = "http://10.0.2.2:3000/api/"
//    private const val BaseURL = "http://192.168.43.238:3000/api/"

    var token: String? = null
    var logged_user: User? =null
    var startTime : String? = null

    private val logger = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    private val okHttp = OkHttpClient.Builder().addInterceptor(logger)
//    private val okHttp = OkHttpClient.Builder()
    private val retrofitBuilder = Retrofit.Builder()
        .baseUrl(Constants.BaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttp.build())

    private val retrofit = retrofitBuilder.build()

    fun <T> buildService(serviceType: Class<T>):T {
        return retrofit.create(serviceType)
    }
}