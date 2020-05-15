package com.example.smkcoding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.smkcoding.data.DataServices
import com.example.smkcoding.data.apiRequests
import com.example.smkcoding.data.httpClient
import com.example.smkcoding.util.BASE_URL_API
import com.example.smkcoding.util.tampilToast
import kotlinx.android.synthetic.main.activity_detail_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPost : AppCompatActivity() {
    var komentarNew: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_post)
        getData()
        btnSend.setOnClickListener {
            komentarNew = komentarText.text.toString()
            when {
                komentarNew.trim().isEmpty() -> komentarText.error = "Wajib diisi"
                else -> sendKomentar()
            }
        }
    }


    fun sendKomentar(){
        val sharedPreference:SharedPreference=SharedPreference(this)
        val bundle = intent.extras
        val id_post = bundle.getString("idPost")

        komentarNew = komentarText.text.toString()

        val idUser = sharedPreference.getValueString(sharedPreference.idUSer)
        val httpClient = httpClient()
        val apiRequest = apiRequests<DataServices>(httpClient)


        val call = apiRequest.postKomentar(id_post,idUser.toString(),komentarNew)
        call.enqueue(object : Callback<PostResponse>{
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.d("erorKOmen",t.toString())
                tampilToast(this@DetailPost, "Gagal kirim komentar")
            }

            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                tampilToast(this@DetailPost, "Sukses komentar")
                getData()
                komentarText.setText("")
            }
        })
    }

    fun getData(){
        val sharedPreference:SharedPreference=SharedPreference(this)
        val bundle = intent.extras
        val id_post = bundle.getString("idPost")

        val idUser = sharedPreference.getValueString(sharedPreference.idUSer)
        val httpClient = httpClient()
        val apiRequest = apiRequests<DataServices>(httpClient)


        val call = apiRequest.getDataPostById(id_post,idUser.toString())
        call.enqueue(object : Callback<dataPostById>{
            override fun onFailure(call: Call<dataPostById>, t: Throwable) {
                Log.d("gagal ambil data post", t.toString())
                tampilToast(this@DetailPost, "gagal ambil data")
            }

            override fun onResponse(call: Call<dataPostById>, response: Response<dataPostById>) {
                when{
                    response.isSuccessful ->
                        when{
                            response.body()?.status.toString() == "200" -> {
                                val dataPost = response.body()?.dataPost

                                poster.text = dataPost?.nama + "'s Post"
                                text.text = dataPost?.text
                                jumlahLikeTv.text = dataPost?.totalLike
                                jumlahKomenTv.text = dataPost?.totalKomen
                                if (dataPost?.img!!.isNotEmpty()) {
                                    Glide.with(this@DetailPost)
                                        .load(BASE_URL_API + "storage/imagePost/" + dataPost?.img)
                                        .into(imageTV)
                                }
                                if (dataPost?.img!!.isNullOrEmpty()){
                                    txtwithoutimage.visibility = LinearLayout.VISIBLE
                                    text2.text = dataPost?.text
                                    text.visibility = TextView.GONE
                                }
                                showPostList(response.body()!!.komentar)
                            }
                            else -> {
                                tampilToast(this@DetailPost, "Cek Jaringan")
                            }
                        }
                    else -> {
                        tampilToast(this@DetailPost, "Gagal")
                    }
                }
            }
        })
    }

    private fun showPostList(komentar: List<Komentar>) {
        rvKomen.layoutManager = LinearLayoutManager(this)
        rvKomen.adapter = KomentarAdapter(this, komentar){

        }

    }
}
