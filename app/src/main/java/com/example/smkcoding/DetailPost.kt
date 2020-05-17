package com.example.smkcoding

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
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
                                if (dataPost?.isLiked == "1"){
                                    jempolImg.setImageResource(R.drawable.ic_thumb_up_blue_24dp)
                                    jumlahLikeTv.setTextColor(Color.BLUE)

                                    jempolImg.setOnClickListener {
                                        likeController(true,dataPost?.idPost)
                                    }
                                }
                                else if (dataPost?.isLiked == "0"){
                                    jempolImg.setOnClickListener {
                                        likeController(false,dataPost?.idPost)
                                    }
                                }
                                if (dataPost?.idUser == idUser){
                                    deleteImg.visibility = ImageView.VISIBLE
                                    deleteImg.setOnClickListener {
                                        val builder = AlertDialog.Builder(this@DetailPost)
                                        builder.setMessage("Hapus Post ini?")
                                            .setPositiveButton("Ok",
                                                DialogInterface.OnClickListener { dialog, which ->  actionDeletePost(dataPost?.idPost)}
                                            )
                                            .setNegativeButton("Batal",
                                                DialogInterface.OnClickListener { dialog, which ->  }
                                            )
                                            .show()
                                        builder.create()
                                    }
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

    private fun actionDeletePost(idPost: String) {
        val httpClient = httpClient()
        val apiRequest = apiRequests<DataServices>(httpClient)

        val call = apiRequest.deletePost(idPost)
        call.enqueue(object: Callback<RegisterUserResponse> {
            override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                tampilToast(this@DetailPost, "Gagal hapus post")
            }

            override fun onResponse(
                call: Call<RegisterUserResponse>,
                response: Response<RegisterUserResponse>
            ) {
                tampilToast(this@DetailPost, "Sukses hapus post")
                val intent = Intent(this@DetailPost, MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                finish()
            }
        })
    }

    private fun likeController(isLiked: Boolean, idPost: String){
        val httpClient = httpClient()
        val apiRequest = apiRequests<DataServices>(httpClient)
        val sharedPreference:SharedPreference=SharedPreference(this)
        val idUser = sharedPreference.getValueString(sharedPreference.idUSer)

        var call = apiRequest.postLike(idPost,idUser.toString())
        if (isLiked == true){
            call = apiRequest.deleteLike(idPost,idUser.toString())
            call.enqueue(object : Callback<RegisterUserResponse> {
                override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                    Log.d("likefailure", t.toString())
                }

                override fun onResponse(
                    call: Call<RegisterUserResponse>,
                    response: Response<RegisterUserResponse>
                ) {
                    Log.d("likesukses", "ok")
                }
            })
            jumlahLikeTv.setTextColor(Color.BLACK)

            jempolImg.setImageResource(R.drawable.ic_thumb_up_black_24dp)
            return getData()
        }
        else if(isLiked == false) {
            call.enqueue(object : Callback<RegisterUserResponse> {
                override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                    Log.d("likefailure", t.toString())
                }

                override fun onResponse(
                    call: Call<RegisterUserResponse>,
                    response: Response<RegisterUserResponse>
                ) {
                    Log.d("likesukses", "ok")
                }
            })
            jumlahLikeTv.setTextColor(Color.BLUE)

            jempolImg.setImageResource(R.drawable.ic_thumb_up_blue_24dp)
            return getData()
        }
    }

    private fun showPostList(komentar: List<Komentar>) {
        rvKomen.layoutManager = LinearLayoutManager(this)
        rvKomen.adapter = KomentarAdapter(this, komentar){

        }

    }
}
