package com.example.smkcoding

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
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
import com.example.smkcoding.util.tampilToast
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

        fun bindItem(item: Data, listener: (Data) -> Unit) {
            image.setImageResource(0)
            image.setImageDrawable(null)
            image.setImageURI(null)

            text.text = item.text
            nama.text = item.nama
            jumlahKomen.text = item.totalKomen
            jumlahLike.text = item.totalLike
            if (item.img.length != 0) {
                image.visibility = ImageView.VISIBLE
                Glide.with(context)
                    .load(BASE_URL_API+"storage/imagePost/" + item.img)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image)
            }


            containerView.setOnClickListener { listener(item) }
        }

        private fun actionDeletePost(idPost: String) {
            val httpClient = httpClient()
            val apiRequest = apiRequests<DataServices>(httpClient)

            val call = apiRequest.deletePost(idPost)
            call.enqueue(object: Callback<RegisterUserResponse> {
                override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                    tampilToast(context!!, "Gagal hapus post")
                }

                override fun onResponse(
                    call: Call<RegisterUserResponse>,
                    response: Response<RegisterUserResponse>
                ) {
                    tampilToast(context!!, "Sukses hapus post")
                }
            })
        }

    }
}