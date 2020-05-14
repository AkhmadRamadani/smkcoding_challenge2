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

class HomeFragment : Fragment(), AdapterView.OnItemSelectedListener {

    var spinnertypeofcontent: Spinner? = null
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
        setDataSpinner()
        floating_action_button.setOnClickListener {
            val intent = Intent(context!!, PostActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        tampilToast(context!!, "nothingSelected")

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val sharedPreference:SharedPreference=SharedPreference(context!!)
        getPost(sharedPreference,types[position].toString())
    }

    private fun setDataSpinner(){

        ArrayAdapter.createFromResource(context!!,R.array.typeOfContentArray,android.R.layout.simple_spinner_item)
            .also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                typeOfContent.adapter = adapter
            }

    }

    private fun getPost(sharedPreference: SharedPreference, tipekonten: String) {
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
                Log.d("FragmentSuccess", response.toString())
                when {
                    response.isSuccessful ->
                        when{
                            response.body()?.data?.size != 0 ->
                                showPostList(response.body()!!.data)
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
//        when {
//            spinnerInput.equals("Recent Posts", ignoreCase = true)->{
//                val call = apiRequest.getLatestPosts("15")
//                call.enqueue(object: Callback<GetPopularData>{
//                    override fun onFailure(call: Call<GetPopularData>, t: Throwable) {
//                        dismissLoading(swipeRefreshLayout)
//                        Log.d("FragmentError", t.toString())
//                        tampilToast(context!!, "Gagallllllll")
//                    }
//
//                    override fun onResponse(call: Call<GetPopularData>, response: Response<GetPopularData>) {
//                        dismissLoading(swipeRefreshLayout)
//                        Log.d("FragmentSuccess", response.toString())
//                        when {
//                            response.isSuccessful ->
//                                when{
//                                    response.body()?.data?.size != 0 ->
//                                        showPostList(response.body()!!.data)
//                                    else -> {
//                                        tampilToast(context!!, "Berhasil")
//                                    }
//                                }
//                            else -> {
//                                tampilToast(context!!, "Gagal")
//                            }
//                        }
//                    }
//
//                })
//            }
//            else ->{
//                val call = apiRequest.getPopularPosts("15")
//                call.enqueue(object: Callback<GetPopularData>{
//                    override fun onFailure(call: Call<GetPopularData>, t: Throwable) {
//                        dismissLoading(swipeRefreshLayout)
//                        Log.d("FragmentError", t.toString())
//                        tampilToast(context!!, "Gagallllllll")
//                    }
//
//                    override fun onResponse(call: Call<GetPopularData>, response: Response<GetPopularData>) {
//                        dismissLoading(swipeRefreshLayout)
//                        Log.d("FragmentSuccess", response.toString())
//                        when {
//                            response.isSuccessful ->
//                                when{
//                                    response.body()?.data?.size != 0 ->
//                                        showPostList(response.body()!!.data)
//                                    else -> {
//                                        tampilToast(context!!, "Berhasil")
//                                    }
//                                }
//                            else -> {
//                                tampilToast(context!!, "Gagal")
//                            }
//                        }
//                    }
//
//                })
//            }
//
//        }


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
