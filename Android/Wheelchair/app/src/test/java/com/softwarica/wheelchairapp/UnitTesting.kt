package com.softwarica.wheelchairapp

import com.softwarica.wheelchairapp.network.api.*
import com.softwarica.wheelchairapp.network.model.Coordinates
import com.softwarica.wheelchairapp.network.model.EndActivity
import com.softwarica.wheelchairapp.network.model.Tracker
import com.softwarica.wheelchairapp.network.repository.ActivityRespository
import com.softwarica.wheelchairapp.network.repository.TrackerRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class UnitTesting : VehicleAPIRequest() {
    var token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjYwZGMxM2QzZmZjYTRmNDkzODU3NWZiMCIsImlhdCI6MTYzMDA4MDA0MywiZXhwIjoxNjMwMTY2NDQzfQ.BvnB83SPBIGxScGw5ePP_7usCgQPrBgHXKsXZru8mmY "
    val authAPI = ServiceBuilder.buildService(AuthAPI::class.java)
    val trackerAPI = ServiceBuilder.buildService(TrackerAPI::class.java)
    val activityAPI = ServiceBuilder.buildService(ActivityAPI::class.java)

    @Test
    fun userLogin() = runBlocking {
        val response = apiRequest {
            authAPI.loginUser("raj@gmail.com", "1234567890")
        }
        token = response.data?.token.toString()
        val expectedResult = true
        val actualResult = response.data !== null
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun startActivityTracking() = runBlocking {
        ServiceBuilder.token = token
        val currentTime = LocalTime.now()
        val startTime = currentTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))
        val activityRespository = ActivityRespository()
        val response = activityRespository.startActivity("60d2a7370cb06b0b2416e66e",startTime)
        val expectedResult = true
        val actualResult = response?.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun endActivityTracking() = runBlocking {
        ServiceBuilder.token = token
        val currentTime = LocalTime.now()
        val endTime = currentTime.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.MEDIUM))
        val activityRespository = ActivityRespository()
        val endActivity = EndActivity("10:38:46 PM","60d2a7370cb06b0b2416e66e", endTime, 18,20)
        val response = activityRespository.endActivity(endActivity)
        val expectedResult = true
        val actualResult = response?.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun locationTracking() = runBlocking{
        ServiceBuilder.token = token
        val trackerRepository = TrackerRepository()
        val tracker = Tracker("60cf00179ea5901d78e39ddc","60d2a7370cb06b0b2416e66e", Coordinates(arrayOf<Double>(27.70571596,85.4)))
        val response = trackerRepository.addTracker(tracker)
        val expectedResult = true
        val actualResult = response?.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun userProfile() = runBlocking {
        val response = apiRequest {
            authAPI.getProfile(token)
        }
        val expectedResult = true
        val actualResult = response.data !== null
        Assert.assertEquals(expectedResult, actualResult)
    }
}