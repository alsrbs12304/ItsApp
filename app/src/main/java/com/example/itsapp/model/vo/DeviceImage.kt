package com.example.itsapp.model.vo

import com.google.gson.annotations.SerializedName

data class DeviceImage(
    @SerializedName("code") val code : String,
    @SerializedName("jsonArray") val jsonArray : List<DeviceImageInfo>
)
data class DeviceImageInfo(
    @SerializedName("DEVICE_NAME") val deviceName : String,
    @SerializedName("DEVICE_IMG") val imgUrl:String
)
