package com.softwarica.wheelchairapp.network.response

import com.softwarica.wheelchairapp.network.model.User


data class UserResponse(
    val success: Boolean? = null,
    val message : String ?= null,
    val data : User? = null
)