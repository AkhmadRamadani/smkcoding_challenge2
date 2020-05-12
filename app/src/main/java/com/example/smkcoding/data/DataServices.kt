package com.example.smkcoding.data

import com.example.smkcoding.*
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DataServices {
    @FormUrlEncoded
    @POST("general/getPostDataLatest")
    fun getLatestPosts(@Field("id_user") id_user: String ):Call<GetPopularData>

    @FormUrlEncoded
    @POST("general/getPostDataPopular")
    fun getPopularPosts(@Field("id_user") id_user: String ):Call<GetPopularData>

    @FormUrlEncoded
    @POST("auth/login")
    fun loginUser(@Field("email") email: String,
                  @Field("password")password: String): Call<LoginResponse>

    @FormUrlEncoded
    @POST("auth/register")
    fun registerUser(@Field("email") email: String,
                  @Field("password")password: String,
                     @Field("name") nama: String): Call<RegisterUserResponse>

    @FormUrlEncoded
    @POST("general/search")
    fun search(@Field("keyword") keyword: String,
               @Field("id_user") id_user: String): Call<SearchResult>
}