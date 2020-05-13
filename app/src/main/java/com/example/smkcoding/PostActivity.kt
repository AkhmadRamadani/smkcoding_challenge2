package com.example.smkcoding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import okhttp3.MultipartBody

class PostActivity : AppCompatActivity() {

    lateinit var img:MultipartBody.Part
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
    }

}
