package com.example.itsapp.model.vo

import com.google.gson.annotations.SerializedName

data class BrandImage(
    @SerializedName("code") val code: String,
    @SerializedName("jsonArray") val brand: List<DeviceBrandInfo>
)
data class DeviceBrandInfo(
    @SerializedName("BRAND_NAME") val name : String,
    @SerializedName("BRAND_IMG") val imgUrl:String
)
