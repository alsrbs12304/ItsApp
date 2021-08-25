package com.example.itsapp.model.vo.favorites

import com.google.gson.annotations.SerializedName

data class Favorites(
    @SerializedName("DEVICE_NAME") var deviceName : String,
    @SerializedName("USER_ID") var userId : String
)