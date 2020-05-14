package com.example.smkcoding.data

import com.example.smkcoding.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
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

    @FormUrlEncoded
    @POST("general/postLike")
    fun postLike(@Field("id_post") id_post: String,
               @Field("id_user") id_user: String): Call<RegisterUserResponse>

    @FormUrlEncoded
    @POST("general/deleteLike")
    fun deleteLike(@Field("id_post") id_post: String,
                 @Field("id_user") id_user: String): Call<RegisterUserResponse>


    @Multipart
    @POST("general/post")
    fun post(@Part img: MultipartBody.Part? = null,
             @Part("text") text: RequestBody,
             @Part("id_user") id_user: RequestBody): Call<PostResponse>
}