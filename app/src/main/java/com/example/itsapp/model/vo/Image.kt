package com.example.itsapp.model.vo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Image(
    @SerializedName("code")
    @Expose
    var code:Int,

    @SerializedName("imageUri")
    @Expose
    var imageUri: String
)
