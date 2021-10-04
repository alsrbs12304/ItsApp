package com.example.itsapp.model.vo.device

import com.example.itsapp.model.vo.device.Device
import com.google.gson.annotations.SerializedName

data class DeviceInfo(
    @SerializedName("code") var code : String,
    @SerializedName("jsonArray") var jsonArray:List<Device>
)