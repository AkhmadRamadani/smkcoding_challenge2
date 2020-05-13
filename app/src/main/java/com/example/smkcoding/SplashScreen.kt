package com.example.smkcoding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class SplashScreen : AppCompatActivity() {
    lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPreference:SharedPreference=SharedPreference(this)

        handler = Handler()
        handler.postDelayed({
            verifying(sharedPreference)
            finish()
        },2000)
    }

    fun verifying(sharedPreference: SharedPreference){
        if (sharedPreference.getValueBoolean(sharedPreference.login) == false){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
        else{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
