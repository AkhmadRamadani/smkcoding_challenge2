package com.example.smkcoding

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.smkcoding.data.DataServices
import com.example.smkcoding.data.apiRequests
import com.example.smkcoding.data.httpClient
import com.example.smkcoding.util.tampilToast
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.card_komen.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.smkcoding.DetailPost as DetailPost1

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
            val sharedPreference:SharedPreference=SharedPreference(context!!)
            val idUser = sharedPreference.getValueString(sharedPreference.idUSer)
            tvNama.text = get.nama
            tvemail.text = get.email
            tvKomen.text = get.komentar
            if (get.idUser == idUser){
                hapusKomen.visibility = ImageView.VISIBLE
                hapusKomen.setOnClickListener {
                    val builder = AlertDialog.Builder(context!!)
                    builder.setMessage("Hapus Komentar ini?")
                        .setPositiveButton("Ok",
                            DialogInterface.OnClickListener { dialog, which ->  actionDeleteKomen(get.idKomentar)}
                        )
                        .setNegativeButton("Batal",
                            DialogInterface.OnClickListener { dialog, which ->  }
                        )
                        .show()
                    builder.create()
                }
            }
            containerView.setOnClickListener { listener(get) }
        }
        fun actionDeleteKomen(idPost: String) {
            val httpClient = httpClient()
            val apiRequest = apiRequests<DataServices>(httpClient)

            val call = apiRequest.deleteKomentar(idPost)
            call.enqueue(object: Callback<RegisterUserResponse> {
                override fun onFailure(call: Call<RegisterUserResponse>, t: Throwable) {
                    tampilToast(context!!, "Gagal hapus komen")
                }

                override fun onResponse(
                    call: Call<RegisterUserResponse>,
                    response: Response<RegisterUserResponse>
                ) {
                    tampilToast(context!!, "Sukses hapus komen")
                    (context as DetailPost1).getData()
                }
            })
        }
    }

}