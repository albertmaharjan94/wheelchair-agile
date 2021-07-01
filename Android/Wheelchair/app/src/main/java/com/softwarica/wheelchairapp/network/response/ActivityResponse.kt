package com.softwarica.wheelchairapp.network.response

import com.softwarica.wheelchairapp.network.model.Activity


data class ActivityResponse(
    val success: Boolean ?= null,
    val activity : Activity ?= null
)