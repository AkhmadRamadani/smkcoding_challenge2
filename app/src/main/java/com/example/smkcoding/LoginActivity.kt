package com.example.smkcoding

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.telecom.Call
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.smkcoding.data.DataServices
import com.example.smkcoding.data.apiRequests
import com.example.smkcoding.data.httpClient
import com.example.smkcoding.util.tampilToast
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var emailNew : String = ""
    private var passwordNew : String = ""
    private var auth: FirebaseAuth? = null
    private val RC_SIGN_IN = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val sharedPreference: SharedPreference = SharedPreference(this)

        btnLogin.setOnClickListener(){
            emailNew = email.text.toString()
            passwordNew = password.text.toString()
            progressBar.visibility = ProgressBar.VISIBLE
            if (validateLogin(emailNew,passwordNew)){
                loginActivity(emailNew, passwordNew, sharedPreference)
            }else{
                progressBar.visibility = ProgressBar.INVISIBLE
            }
        }
        btnRegister.setOnClickListener {
            val intent = Intent(this,RegisterActivity::class.java)

            startActivity(intent)
            finish()
        }
        lgnWithGoogle.setOnClickListener(this)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == RC_SIGN_IN){
            if (resultCode == Activity.RESULT_OK){
                val user = FirebaseAuth.getInstance().currentUser
                loginWithGoogle(user!!.email.toString() ,user!!.uid.toString(), user!!.displayName.toString())

            }else{
            }
        }
    }
    override fun onClick(v: View?) {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(listOf(AuthUI.IdpConfig.GoogleBuilder().build()))
                .setIsSmartLockEnabled(false)
                .build(),
            RC_SIGN_IN)

        progressBar.visibility = ProgressBar.VISIBLE
    }

    fun loginWithGoogle(emailNew: String, passwordNew: String, namaNew: String){
        val httpClient = httpClient()
        val apiRequest = apiRequests<DataServices>(httpClient)
        val call = apiRequest.registerUser(emailNew,passwordNew,namaNew);

        val sharedPreference: SharedPreference = SharedPreference(this)

        call.enqueue(object : Callback<RegisterUserResponse>{
            override fun onFailure(call: retrofit2.Call<RegisterUserResponse>, t: Throwable) {
                tampilToast(this@LoginActivity, "Periksa Jaringan")
                progressBar.visibility = ProgressBar.INVISIBLE

                Log.d("gagal regg", t.toString())
            }

            override fun onResponse(
                call: retrofit2.Call<RegisterUserResponse>,
                response: Response<RegisterUserResponse>
            ) {
                when{
                    response.isSuccessful->{
                        when{
                            response.body()?.status == false->{
                                progressBar.visibility = ProgressBar.INVISIBLE
                                loginActivity(emailNew,passwordNew,sharedPreference)
//                                tampilToast(this@LoginActivity, response.body()?.message.toString())

                            }
                            else -> {
                                progressBar.visibility = ProgressBar.INVISIBLE
                                loginActivity(emailNew,passwordNew,sharedPreference)

//                                tampilToast(this@LoginActivity, response.body()?.message.toString())
                            }
                        }
                    }
                    else -> {
                        progressBar.visibility = ProgressBar.INVISIBLE

                        tampilToast(this@LoginActivity, "Register Gagal")
                    }
                }
            }

        })
    }

    fun loginActivity(emailparams: String, passwordparams: String, sharedPreference: SharedPreference){
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
                            response.body()?.status == true && response.body()?.data!!.verified == "1" -> {
                                sharedPreference.saveString(sharedPreference.apiKey, response.body()?.data!!.apiKey)
                                sharedPreference.saveString(sharedPreference.nama, response.body()?.data!!.nama)
                                sharedPreference.saveString(sharedPreference.email, response.body()?.data!!.email)
                                sharedPreference.saveString(sharedPreference.idUSer, response.body()?.data!!.idUser)
                                sharedPreference.saveString(sharedPreference.password, response.body()?.data!!.password)
                                sharedPreference.saveString(sharedPreference.verified, response.body()?.data!!.verified)
                                sharedPreference.saveBoolean(sharedPreference.login, true)

                                val intent = Intent(this@LoginActivity,MainActivity::class.java)
                                startActivity(intent)
                                progressBar.visibility = ProgressBar.INVISIBLE

                                finish()
                            }
                            response.body()?.data!!.verified == "0" -> {
                                progressBar.visibility = ProgressBar.INVISIBLE
                                tampilToast(this@LoginActivity,"Lihat Email dan verifikasi akun anda")
                            }
                            else  -> {
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
