package com.example.smkcoding

import android.content.Context
import android.content.SharedPreferences

class SharedPreference(val context: Context) {

    public val apiKey : String = "apiKey"
    public val email : String = "email"
    public val idUSer : String = "idUser"
    public val nama : String = "nama"
    public val password : String = "password"
    public val verified : String = "verified"
    public val login: String = "login"

    val sharedPref: SharedPreferences = context.getSharedPreferences("REFS_NAME", Context.MODE_PRIVATE)

    fun saveString(KEY_NAME: String, value: String){
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(KEY_NAME, value)
        editor.apply()
    }

    fun saveInt(KEY_NAME: String, value: Int){
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putInt(KEY_NAME, value)
        editor.apply()
    }

    fun saveBoolean(KEY_NAME: String, value: Boolean){
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(KEY_NAME, value)
        editor.apply()
    }

    fun getValueString(KEY_NAME: String): String?{
        return sharedPref.getString(KEY_NAME, null)
    }

    fun getValueInt(KEY_NAME: String): Int?{
        return sharedPref.getInt(KEY_NAME, 0)
    }

    fun getValueBoolean(KEY_NAME: String): Boolean?{
        return sharedPref.getBoolean(KEY_NAME, false)
    }

    fun clearSharedPreference() {

        val editor: SharedPreferences.Editor = sharedPref.edit()

        editor.clear()
        editor.apply()
    }

}