package com.example.smkcoding.data

import com.example.smkcoding.Data
import com.example.smkcoding.HomeFragment
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface DataServices {
    @POST("general/getPostDataLatest")
    @FormUrlEncoded
    fun getLatestPosts(@Field("id_user") id_user: String ):Call<List<Data>>
}