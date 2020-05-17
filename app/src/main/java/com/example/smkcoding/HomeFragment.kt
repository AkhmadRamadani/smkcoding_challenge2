package com.example.smkcoding

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
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

    val types = arrayOf("Recent Posts", "Popular Posts")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        typeOfContent.onItemSelectedListener
        return inflater.inflate(R.layout.fragment_home, container, false)

    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreference:SharedPreference=SharedPreference(context!!)

        getPost(sharedPreference,types[0].toString())

        floating_action_button.setOnClickListener {
            val intent = Intent(context!!, PostActivity::class.java)
            startActivity(intent)
        }
    }

    fun getPost(sharedPreference: SharedPreference, tipekonten: String) {
        showLoading(context!!, swipeRefreshLayout)
        var spinnerInput = tipekonten
        Log.d("SpinnerValue", spinnerInput)
        val idUser = sharedPreference.getValueString(sharedPreference.idUSer)
        val httpClient = httpClient()
        val apiRequest = apiRequests<DataServices>(httpClient)


        val call = apiRequest.getLatestPosts(idUser.toString())
        call.enqueue(object: Callback<GetPopularData>{
            override fun onFailure(call: Call<GetPopularData>, t: Throwable) {
                dismissLoading(swipeRefreshLayout)
                Log.d("FragmentError", t.toString())
                tampilToast(context!!, "Gagallllllll")
            }

            override fun onResponse(call: Call<GetPopularData>, response: Response<GetPopularData>) {
                dismissLoading(swipeRefreshLayout)
                Log.d("FragmentSuccess", response.body()?.toString())
                when {
                    response.isSuccessful ->
                        when{
                            response.body()?.data != null ->
                                showPostList(response.body()!!.data)
                            else -> {
                                tampilToast(context!!, "DataKosong")
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
            val intent = Intent(context!!, DetailPost::class.java)
            val bundle = Bundle()
            bundle.putString("idPost", newdata.idPost)
            intent.putExtras(bundle)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}
