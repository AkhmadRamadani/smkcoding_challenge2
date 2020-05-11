package com.example.smkcoding

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_post.*

class PostListsAdapter(private val context: Context, private val items: List<Data>
,private val listener: (Data)-> Unit): RecyclerView.Adapter<PostListsAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.card_post,parent,false))

    override fun getItemCount(): Int = items?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items.get(position), listener)
    }

    class ViewHolder(val context: Context, override val containerView: View):
        RecyclerView.ViewHolder(containerView), LayoutContainer{
        fun bindItem(item: Data, listener: (Data) -> Unit) {

            text.text = item.text
            nama.text = item.nama
            if (item.img.length != 0) {
                Glide.with(context)
                    .load("http://192.168.0.21/joke/v1/storage/imagePost/" + item.img).into(image)
            }

            containerView.setOnClickListener { listener(item) }
        }
    }
}