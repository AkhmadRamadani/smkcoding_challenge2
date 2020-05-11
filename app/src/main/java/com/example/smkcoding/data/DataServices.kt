package com.example.smkcoding.data

import com.example.smkcoding.Data
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DataServices {
    @FormUrlEncoded
    @POST("general/getPostDataLatest")
    fun getLatestPosts(@Field("id_user") id_user: String ):Call<List<Data>>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(@Field("email") email: String,
                  @Field("password")password: String): Call<ResponseBody>
}