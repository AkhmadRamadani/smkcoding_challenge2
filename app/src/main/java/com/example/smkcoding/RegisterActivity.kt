package com.example.smkcoding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import com.example.smkcoding.data.DataServices
import com.example.smkcoding.data.apiRequests
import com.example.smkcoding.data.httpClient
import com.example.smkcoding.util.tampilToast
import kotlinx.android.synthetic.main.activity_register.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private var emailNew : String = ""
    private var passwordNew : String = ""
    private var namaNew: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        backToLogin.setOnClickListener {
            val inten = Intent(this, LoginActivity::class.java)
            startActivity(inten)
            finish()
        }
        btnRegister.setOnClickListener {
            emailNew = email.text.toString()
            passwordNew = password.text.toString()
            namaNew = nama.text.toString()
            progressBar.visibility = ProgressBar.VISIBLE

            if (validateLogin(emailNew,passwordNew,namaNew)){
                registerRequest(emailNew,passwordNew,namaNew)
            }else{
                progressBar.visibility = ProgressBar.INVISIBLE

            }
        }
    }

    private fun registerRequest(emailNew: String, passwordNew: String, namaNew: String) {
        val httpClient = httpClient()
        val apiRequest = apiRequests<DataServices>(httpClient)
        val call = apiRequest.registerUser(emailNew,passwordNew,namaNew);

        call.enqueue(object : Callback<RegisterUserResponse>{
            override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                tampilToast(this@RegisterActivity, "Periksa Jaringan")
                progressBar.visibility = ProgressBar.INVISIBLE

                Log.d("gagal regg", t.toString())
            }

            override fun onResponse(
                call: Call<RegisterUserResponse>,
                response: Response<RegisterUserResponse>
            ) {
                when{
                    response.isSuccessful->{
                        when{
                            response.body()?.status == true->{
                                tampilToast(this@RegisterActivity, "Pendaftaran berhasil, silahkan login")
                                email.setText("")
                                password.setText("")
                                nama.setText("")
                                progressBar.visibility = ProgressBar.INVISIBLE
                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }
                            else -> {
                                progressBar.visibility = ProgressBar.INVISIBLE

                                tampilToast(this@RegisterActivity, response.body()?.message.toString())
                            }
                        }
                    }
                    else -> {
                        progressBar.visibility = ProgressBar.INVISIBLE

                        tampilToast(this@RegisterActivity, "Register Gagal")
                    }
                }
            }

        })
    }

    private fun validateLogin(emailNew: String, passwordNew: String, namaNew: String): Boolean {
        if(namaNew == null || namaNew.toString().trim().length == 0){
            nama.error = "Nama wajib diisi"
            return false
        }
        if(emailNew == null || emailNew.toString().trim().length == 0){
            email.error = "Email wajib diisi"
            return false
        }
        if (!emailNew.contains("@") || !emailNew.contains(".")){
            email.error = "Email tidak benar"
            return false
        }
        if(passwordNew == null || passwordNew.toString().trim().length == 0){
            password.error = "Password wajib diisi"
            return false
        }
        if(passwordNew.toString().trim().length < 6){
            password.error = "Password harus 6 karakter"
            return false
        }

        return true
    }
}
