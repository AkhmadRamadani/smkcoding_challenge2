package com.example.smkcoding

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.example.smkcoding.data.DataServices
import com.example.smkcoding.data.apiRequests
import com.example.smkcoding.data.httpClient
import com.example.smkcoding.util.BASE_URL_API
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_post.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PostListsAdapter(private val context: Context, private val items: List<Data>
                       , private val listener: (Data)-> Unit): RecyclerView.Adapter<PostListsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.card_post,parent,false))

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items.get(position), listener)

    }

    class ViewHolder(val context: Context, override val containerView: View):
        RecyclerView.ViewHolder(containerView), LayoutContainer{
        var liked: String = "0"
        var newJL: String = "0"
        fun bindItem(item: Data, listener: (Data) -> Unit) {
            image.setImageResource(0)
            image.setImageDrawable(null)
            image.setImageURI(null)
            text.text = item.text
            nama.text = item.nama
            newJL = item.totalLike
            jumlahKomen.text = item.totalKomen
            jumlahLike.text = newJL
//            Log.d("Img length", image.drawable)
            if (item.img.length != 0) {
                image.visibility = ImageView.VISIBLE
                Glide.with(context)
                    .load(BASE_URL_API+"storage/imagePost/" + item.img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image)
            }
//            if (item.img.length == 0){
//                txtwithoutimage.visibility = LinearLayout.VISIBLE
//                text2.text = item.text
//                text.visibility = TextView.GONE
//            }
//            if(item.text.length == 0){
//                text.visibility = TextView.GONE
//            }
            liked = item.isLiked.toString()
            if (liked == "1"){
                jempol!!.setImageResource(R.drawable.ic_thumb_up_blue_24dp)
                jempol.setOnClickListener {
                    deleteLike(item, listener)
                }
            }
            else if (liked == "0"){
                jempol!!.setImageResource(R.drawable.ic_thumb_up_black_24dp)
                jempol.setOnClickListener {
                    addLike(item, listener)
                }
            }


            containerView.setOnClickListener { listener(item) }
        }
        fun addLike(item: Data, listener: (Data) -> Unit){
            val sharedPreference:SharedPreference=SharedPreference(context!!)
            val idUser = sharedPreference.getValueString(sharedPreference.idUSer)
            val httpClient = httpClient()
            val apiRequest = apiRequests<DataServices>(httpClient)

            val call = apiRequest.postLike(item.idPost.toString(), idUser.toString())
            call.enqueue(object : Callback<RegisterUserResponse>{
                override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<RegisterUserResponse>,
                    response: Response<RegisterUserResponse>
                ) {
                    jempol!!.setImageResource(R.drawable.ic_thumb_up_blue_24dp)
                    liked = "1"
                    newJL = (Integer.parseInt(newJL) + 1).toString()
                    jumlahLike.text = newJL
                    Log.d("nmbhlike", newJL)
                }

            })
        }
        fun deleteLike(item: Data, listener: (Data) -> Unit){
            val sharedPreference:SharedPreference=SharedPreference(context!!)
            val idUser = sharedPreference.getValueString(sharedPreference.idUSer)
            val httpClient = httpClient()
            val apiRequest = apiRequests<DataServices>(httpClient)

            val call = apiRequest.deleteLike(item.idPost.toString(), idUser.toString())
            call.enqueue(object : Callback<RegisterUserResponse>{
                override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {

                }

                override fun onResponse(
                    call: Call<RegisterUserResponse>,
                    response: Response<RegisterUserResponse>
                ) {
                    jempol!!.setImageResource(R.drawable.ic_thumb_up_black_24dp)
                    liked = "0"

                    newJL = (Integer.parseInt(newJL) - 1).toString()
                    jumlahLike.text = newJL

                    Log.d("ngrnglike", newJL)
                }

            })
        }
    }
}