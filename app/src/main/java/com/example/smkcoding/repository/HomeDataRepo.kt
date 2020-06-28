package com.example.smkcoding.repository

import androidx.lifecycle.LiveData
import com.example.smkcoding.Data
import com.example.smkcoding.DataModel
import com.example.smkcoding.dao.HomeDataDao
import com.example.smkcoding.entity.HomeDataModel

class HomeDataRepo(private val homeDataDao: HomeDataDao) {
    val allPost: LiveData<List<HomeDataModel>> = homeDataDao.getPostData()

    suspend fun insert(post: HomeDataModel){
        homeDataDao.insert(post)
    }
    suspend fun insertAll(posts: List<HomeDataModel>){
        homeDataDao.insertAll(posts)
    }
    suspend fun deleteAll() {
        homeDataDao.deleteAll()
    }

    suspend fun update(post: HomeDataModel) {
        homeDataDao.update(post)
    }

    suspend fun delete(post: HomeDataModel) {
        homeDataDao.delete(post)
    }

}