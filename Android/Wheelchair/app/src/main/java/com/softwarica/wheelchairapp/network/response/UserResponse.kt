package com.softwarica.wheelchairapp.network.response

import com.softwarica.wheelchairapp.network.model.User


data class UserResponse(
    val success: Boolean? = null,
    val data : User? = null
)