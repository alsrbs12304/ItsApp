package com.example.itsapp.model.vo.comment

import com.example.itsapp.model.vo.comment.Comment
import com.google.gson.annotations.SerializedName

data class CommentInfo (
    @SerializedName("code") var code : String,
    @SerializedName("jsonArray") var jsonArray:List<Comment>
    )