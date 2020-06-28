package com.example.smkcoding.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smkcoding.Data
import com.example.smkcoding.DataModel
import com.example.smkcoding.db.PostDatabase
import com.example.smkcoding.entity.HomeDataModel
import com.example.smkcoding.repository.HomeDataRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragmentViewModel(): ViewModel()  {
    lateinit var repository: HomeDataRepo
    lateinit var allPost: LiveData<List<HomeDataModel>>

    fun init(context: Context){
        val homeDataDao = PostDatabase.getDatabase(context).homeDataDao()
        repository = HomeDataRepo(homeDataDao)
        allPost = repository.allPost
    }
    fun delete(post: HomeDataModel) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(post)
    }
    fun insertAll(post: List<HomeDataModel>) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteAll()
        repository.insertAll(post)
    }
}