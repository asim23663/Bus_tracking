package com.uni.onclicklgubus.model

import java.io.Serializable

data class Bus(
    val id: String? = "",
    val busNumber: String? = "",
    val driverName: String? = "",
    val lat: Double? = 0.0,
    val lng: Double? = 0.0,
    val busRoute: String? = "",

    ): Serializable