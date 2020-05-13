package com.example.smkcoding

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smkcoding.data.DataServices
import com.example.smkcoding.data.apiRequests
import com.example.smkcoding.data.httpClient
import com.example.smkcoding.util.dismissLoading
import com.example.smkcoding.util.showLoading
import com.example.smkcoding.util.tampilToast
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_search.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    private var keywordText : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, @Nullable savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreference:SharedPreference=SharedPreference(context!!)

        btnSearch.setOnClickListener {
            keywordText = keywordSearch.text.toString()
            getPost(keywordText, sharedPreference)
        }
    }

    private fun getPost(keyword: String, sharedPreference: SharedPreference) {
        val httpClient = httpClient()
        val apiRequest = apiRequests<DataServices>(httpClient)
        val idUser = sharedPreference.getValueString(sharedPreference.idUSer)
        val call = apiRequest.search(keyword,idUser.toString())
        call.enqueue(object: Callback<SearchResult> {
            override fun onFailure(call: Call<SearchResult>, t: Throwable) {
                Log.d("FragmentError", t.toString())
                tampilToast(context!!, "Gagallllllll")
            }

            override fun onResponse(call: Call<SearchResult>, response: Response<SearchResult>) {
                Log.d("FragmentSuccess", response.toString())
                when {
                    response.isSuccessful ->
                        when{
                            response.body()?.joke?.size != 0 ->{
                                kosongPost.visibility = TextView.INVISIBLE
                                showPostList(response.body()!!.joke)
                            }
                            response.body()?.user?.size != 0 -> {
                                kosongUser.visibility = TextView.INVISIBLE
                                showUserList(response.body()!!.user)
                            }
                            else -> {
                                kosongUser.visibility = TextView.VISIBLE
                                showUserList(response.body()!!.user)
                                kosongPost.visibility = TextView.VISIBLE
                                showPostList(response.body()!!.joke)
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

    private fun showUserList(data: List<UserSearchResult>){
        val layoutManager = FlexboxLayoutManager(context)
//        layoutManager.flexDirection = FlexDirection.COLUMN
//        layoutManager.justifyContent = JustifyContent.FLEX_END
//        val layoutManager = LinearLayoutManager(context)
        userList.layoutManager = layoutManager
        userList.adapter = UserListsAdapter(context!!, data){
            val newdata = it
            tampilToast(context!!, newdata.nama)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }
}