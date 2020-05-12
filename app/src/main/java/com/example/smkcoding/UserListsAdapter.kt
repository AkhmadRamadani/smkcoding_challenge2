package com.example.smkcoding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_user.*

class UserListsAdapter(private val context: Context, private val items: List<UserSearchResult>
,private val listener: (UserSearchResult)-> Unit): RecyclerView.Adapter<UserListsAdapter.ViewHolder>(){
    class ViewHolder(val context: Context, override val containerView: View): RecyclerView.ViewHolder(containerView)
    ,LayoutContainer{
        fun bindItem(item: UserSearchResult, listener: (UserSearchResult) -> Unit){
            nama.text = item.nama
            email.text = item.email
            totalPost.text = item.totalPost

            containerView.setOnClickListener { listener(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.card_user, parent, false))

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items.get(position), listener)
    }

}