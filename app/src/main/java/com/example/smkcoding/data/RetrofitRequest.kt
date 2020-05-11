package com.example.smkcoding.data

import com.example.smkcoding.R
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun httpClient(): OkHttpClient{
    val loggingInterceptor= HttpLoggingInterceptor()
    loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

    val builder = OkHttpClient.Builder()

    builder.readTimeout(15, TimeUnit.SECONDS)
    builder.connectTimeout(15, TimeUnit.SECONDS)
    builder.addInterceptor(loggingInterceptor)
    return builder.build()
}

inline fun <reified T>apiRequests(okHttpClient: OkHttpClient):T{
    val gson = GsonBuilder().create()

    val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.43.247/joke/v1/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()
        return retrofit.create(T::class.java)
}