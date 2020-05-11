package com.example.smkcoding


import com.google.gson.annotations.SerializedName

data class LatestPost(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("status")
    val status: String
)