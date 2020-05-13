package com.example.smkcoding.data

import com.example.smkcoding.*
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.*

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

    @Multipart
    @POST("general/post")
    fun post(@Part("img") img:MultipartBody.Part,
             @Field("text") text: String,
             @Field("id_user") id_user: String): Call<PostResponse>
}