package com.example.smkcoding

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_komen.*

class KomentarAdapter(private val context: Context, private val items: List<Komentar>,
                      private val listener: (Komentar) -> Unit): RecyclerView.Adapter<KomentarAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int)
            = ViewHolder(context, LayoutInflater.from(context).inflate(R.layout.card_komen,parent,false))

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(items?.get(position), listener)
    }
    class ViewHolder(val context: Context, override val containerView: View):
        RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bindItem(get: Komentar, listener: (Komentar) -> Unit) {
            tvNama.text = get.nama
            tvemail.text = get.email
            tvKomen.text = get.komentar
            containerView.setOnClickListener { listener(get) }
        }

    }

}