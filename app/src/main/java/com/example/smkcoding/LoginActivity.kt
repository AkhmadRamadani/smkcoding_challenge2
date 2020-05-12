package com.example.smkcoding

import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.smkcoding.data.DataServices
import com.example.smkcoding.data.apiRequests
import com.example.smkcoding.data.httpClient
import com.example.smkcoding.util.tampilToast
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : AppCompatActivity() {
    private var emailNew : String = ""
    private var passwordNew : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        btnLogin.setOnClickListener(){
            emailNew = email.text.toString()
            passwordNew = password.text.toString()
            progressBar.visibility = ProgressBar.VISIBLE
            if (validateLogin(emailNew,passwordNew)){
                loginActivity(emailNew, passwordNew)
            }else{
                progressBar.visibility = ProgressBar.INVISIBLE
            }
        }
        btnRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)

            startActivity(intent)
            finish()
        }
    }

    fun loginActivity(emailparams: String, passwordparams: String){
        val httpClient = httpClient()
        val apiRequest = apiRequests<DataServices>(httpClient)
        val call = apiRequest.loginUser(emailparams,passwordparams)
        call.enqueue(object : Callback<LoginResponse>{
            override fun onFailure(call: retrofit2.Call<LoginResponse>, t: Throwable) {
                Toast.makeText(this@LoginActivity,"Periksa Jaringan",Toast.LENGTH_SHORT).show()
                progressBar.visibility = ProgressBar.INVISIBLE

            }

            override fun onResponse(
                call: retrofit2.Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {
                Log.d("responseee", response.toString())
                when{
                    response.isSuccessful -> {
                        when{
                            response.body()?.status == true -> {
                                val intent = Intent(this@LoginActivity,MainActivity::class.java)
                                startActivity(intent)
                                progressBar.visibility = ProgressBar.INVISIBLE

                                finish()
                            }
                            else -> {
                                progressBar.visibility = ProgressBar.INVISIBLE

                                tampilToast(this@LoginActivity, "Gagal Login")
                            }
                        }
                    }
                    else -> {
                        progressBar.visibility = ProgressBar.INVISIBLE

                        tampilToast(this@LoginActivity,"Gagal Login")
                    }
                }
            }

        })

    }

    fun validateLogin(emailparams: String, passwordparams: String): Boolean {
//        progressBar.visibility = ProgressBar.INVISIBLE

        if(emailparams == null || emailparams.toString().trim().length == 0){

            email.error = "Email wajib diisi"
            return false
        }
        if(passwordparams == null || passwordparams.toString().trim().length == 0){
           password.error = "Password wajib diisi"
            return false
        }
        return true
    }
}
