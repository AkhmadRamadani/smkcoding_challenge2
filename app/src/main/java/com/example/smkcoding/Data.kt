package com.example.smkcoding


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("email")
    val email: String,
    @SerializedName("id_post")
    val idPost: String,
    @SerializedName("id_user")
    val idUser: String,
    @SerializedName("img")
    val img: String,
    @SerializedName("isLiked")
    val isLiked: String,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("text")
    val text: String,
    @SerializedName("totalKomen")
    val totalKomen: String,
    @SerializedName("totalLike")
    val totalLike: String
)