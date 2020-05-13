package com.example.smkcoding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_profil.*

class ProfilFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profil, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreference:SharedPreference=SharedPreference(context!!)
        getData(sharedPreference)
        btnLogout.setOnClickListener {
            logOut(sharedPreference)
        }
    }

    fun getData(sharedPreference: SharedPreference){
        val name = sharedPreference.getValueString(sharedPreference.nama)
        val email = sharedPreference.getValueString(sharedPreference.email)

        nama.text = name
        emailtv.text = email
    }
    fun logOut(sharedPreference: SharedPreference){
        sharedPreference.clearSharedPreference()
        val intent = Intent(context!!, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }
}