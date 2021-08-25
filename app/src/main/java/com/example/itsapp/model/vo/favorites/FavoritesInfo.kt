package com.example.itsapp.model.vo.favorites

import com.example.itsapp.model.vo.favorites.Favorites
import com.google.gson.annotations.SerializedName

data class FavoritesInfo(
    @SerializedName("code") var code : String,
    @SerializedName("jsonArray") var jsonArray: Favorites
)