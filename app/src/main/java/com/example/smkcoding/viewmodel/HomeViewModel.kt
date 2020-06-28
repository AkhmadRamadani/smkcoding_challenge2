package com.example.smkcoding.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.smkcoding.db.PostDatabase
import com.example.smkcoding.entity.HomeDataModel
import com.example.smkcoding.repository.HomeDataRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(): ViewModel()  {
    lateinit var repository: HomeDataRepo
    fun init(context: Context){
        val homeDataDao = PostDatabase.getDatabase(context).homeDataDao()
        repository = HomeDataRepo(homeDataDao)
    }
    fun addData(post: HomeDataModel) = viewModelScope.launch(Dispatchers.IO){
        repository.insert(post)
    }
}