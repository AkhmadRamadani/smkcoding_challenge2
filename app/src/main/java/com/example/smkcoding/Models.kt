package com.example.smkcoding


import com.google.gson.annotations.SerializedName

data class GetPopularData(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("status")
    val status: String
)

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

data class LoginResponse(
    @SerializedName("data")
    val `data`: DataLogin,
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("token")
    val token: String
)

data class DataLogin(
    @SerializedName("api_key")
    val apiKey: String,
    @SerializedName("email")
    val email: String,
    @SerializedName("id_user")
    val idUser: String,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("verified")
    val verified: String
)

data class RegisterUserResponse(
    @SerializedName("status")
    val status: Boolean,
    @SerializedName("message")
    val message: String
)

data class SearchResult(
    @SerializedName("joke")
    val joke: List<Data>,
    @SerializedName("user")
    val user: List<UserSearchResult>
)

data class UserSearchResult(
    @SerializedName("email")
    val email: String,
    @SerializedName("id_user")
    val idUser: String,
    @SerializedName("nama")
    val nama: String,
    @SerializedName("totalPost")
    val totalPost: String
)

data class PostResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("data")
    val data: Boolean
)