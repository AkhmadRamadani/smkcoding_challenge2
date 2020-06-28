package com.example.smkcoding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.smkcoding.data.DataServices
import com.example.smkcoding.data.apiRequests
import com.example.smkcoding.data.httpClient
import com.example.smkcoding.entity.HomeDataModel
import com.example.smkcoding.util.dismissLoading
import com.example.smkcoding.util.showLoading
import com.example.smkcoding.util.tampilToast
import com.example.smkcoding.viewmodel.HomeFragmentViewModel
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : Fragment(), PostListsAdapter.PostDataListener {

    val types = arrayOf("Recent Posts", "Popular Posts")
    var postData: MutableList<HomeDataModel> = ArrayList()
    private val viewModel by viewModels<HomeFragmentViewModel>()
    private var adapter: PostListsAdapter? = null

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
        val sharedPreference:SharedPreference=SharedPreference(requireContext())
        init()
        getPost(sharedPreference,types[0].toString())
        viewModel.init(requireContext());
        viewModel.allPost.observe(viewLifecycleOwner, Observer { postData ->
            postData?.let { adapter }
        })
        floating_action_button.setOnClickListener {
            val intent = Intent(requireContext(), PostActivity::class.java)
            startActivity(intent)
        }
    }

    private fun init(){
        postList.layoutManager = LinearLayoutManager(context)
        adapter = PostListsAdapter(requireContext(), postData as ArrayList<HomeDataModel>, this)
        postList.adapter = adapter
    }

    fun getPost(sharedPreference: SharedPreference, tipekonten: String) {
        showLoading(requireContext(), swipeRefreshLayout)
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
                            response.body()?.data != null -> {
//                                viewModel.insertAll(response.body()?.data)
                                showPostList(response.body()!!.data)
//                                viewModel.insertAll()
                            }
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

    private fun showPostList(data: MutableList<HomeDataModel>){
        adapter?.items = data as ArrayList<HomeDataModel>
        adapter?.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        this.clearFindViewByIdCache()
    }

    override fun onClick(item: HomeDataModel) {
        val intent = Intent(requireContext(), DetailPost::class.java)
        val bundle = Bundle()
        bundle.putString("idPost", item.idPost)
        intent.putExtras(bundle)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }
}
