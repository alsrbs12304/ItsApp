package com.example.itsapp.model.vo

import com.google.gson.annotations.SerializedName

data class SpecInfo(
    @SerializedName("code") var code : String,
    @SerializedName("jsonArray") var jsonArray:List<Spec>
)