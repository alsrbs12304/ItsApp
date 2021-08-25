package com.example.itsapp.model.vo.review

import com.google.gson.annotations.SerializedName

data class ReviewInfo(
    @SerializedName("code") var code : String,
    @SerializedName("jsonArray") var jsonArray:List<Review>
    )