package com.example.itsapp.model.vo.review

import com.google.gson.annotations.SerializedName

data class Review(
    @SerializedName("DEVICE_BRAND") var deviceBrand : String,
    @SerializedName("DEVICE_NAME") var deviceName: String,
    @SerializedName("DEVICE_IMG") var imgurl : String,
    @SerializedName("USER_NICKNAME") var writer: String,
    @SerializedName("REVIEW_POINT") var reviewPoint: Int,
    @SerializedName("CONTENT_PROS") var contentPros: String,
    @SerializedName("CONTENT_CONS") var contentCons: String,
    @SerializedName("COMMENT_COUNT") var commentCount : Int,
    @SerializedName("WRITE_TIME") var writeTime : String
)