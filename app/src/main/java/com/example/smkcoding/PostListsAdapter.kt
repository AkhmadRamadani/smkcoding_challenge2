package com.example.smkcoding

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.smkcoding.util.BASE_URL_API
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_post.*

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

            text.text = item.text
            nama.text = item.nama
            jumlahKomen.text = item.totalKomen
            jumlahLike.text = item.totalLike
            Log.d("Img length", item.img.length.toString())
            if (item.img.length != 0) {
                image.visibility = ImageView.VISIBLE
                Glide.with(context)
                    .load(BASE_URL_API+"storage/imagePost/" + item.img)
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .into(image)
            }

            containerView.setOnClickListener { listener(item) }
        }
    }
}