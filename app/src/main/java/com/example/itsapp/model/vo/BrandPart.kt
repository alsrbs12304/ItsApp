package com.example.itsapp.model.vo

import com.example.itsapp.model.vo.device.Device
import com.google.gson.annotations.SerializedName

data class BrandPart(
    @SerializedName("code") val code: String,
    @SerializedName("brand") val brand: List<Device>
)