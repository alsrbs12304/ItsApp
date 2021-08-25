package com.example.itsapp.model.vo.spec

import com.google.gson.annotations.SerializedName

data class Spec(
    @SerializedName("DEVICE_NAME") var deviceName : String,
    @SerializedName("DEVICE_SPECS") var deviceSpecs : String,
    @SerializedName("DEVICE_OS") var deviceOs : String,
    @SerializedName("DEVICE_CPU") var deviceCpu : String,
    @SerializedName("DEVICE_MEMORY") var deviceMemory : String,
    @SerializedName("DEVICE_SSD") var deviceSsd : String,
    @SerializedName("DEVICE_DISPLAY") var deviceDisplay : String,
    @SerializedName("DEVICE_ETC_SPEC") var deviceEtcSpec : String
)
