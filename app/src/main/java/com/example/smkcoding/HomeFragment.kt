package com.example.smkcoding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smkcoding.data.DataServices
import com.example.smkcoding.data.apiRequests
import com.example.smkcoding.data.httpClient
import com.example.smkcoding.util.dismissLoading
import com.example.smkcoding.util.showLoading
import com.example.smkcoding.util.tampilToast
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getPost()
    }

    private fun getPost() {
        showLoading(context!!, swipeRefreshLayout)

        val httpClient = httpClient()
        val apiRequest = apiRequests<DataServices>(httpClient)

        val call = apiRequest.getLatestPosts("15")
        call.enqueue(object: Callback<List<Data>>{
            override fun onFailure(call: Call<List<Data>>, t: Throwable) {
                dismissLoading(swipeRefreshLayout)
                Log.d("FragmentError", t.toString())
                tampilToast(context!!, "Gagallllllll")
            }

            override fun onResponse(call: Call<List<Data>>, response: Response<List<Data>>) {
                dismissLoading(swipeRefreshLayout)
                Log.d("FragmentSuccess", response.toString())
                when {
                    response.isSuccessful ->
                        when{
                            response.body()?.size != 0 ->
                                showPostList(response.body()!!)
                            else -> {
                                tampilToast(context!!, "Berhasil")
                            }
                        }
                    else -> {
                        tampilToast(context!!, "Gagal")
                    }
                }
            }

        })
    }

    private fun showPostList(data: List<Data>){
        postList.layoutManager = LinearLayoutManager(context)
        postList.adapter = PostListsAdapter(context!!, data){
            val newdata = it
            tampilToast(context!!, newdata.nama )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}