package com.example.smkcoding.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.smkcoding.Data
import com.example.smkcoding.DataModel
import com.example.smkcoding.entity.HomeDataModel

@Dao
interface HomeDataDao {
    @Query("Select * FROM post")
    fun getPostData(): LiveData<List<HomeDataModel>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(post: HomeDataModel)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(posts: List<HomeDataModel>)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(post: HomeDataModel)

    @Delete()
    suspend fun delete(post: HomeDataModel)

    @Query("DELETE FROM post")
    suspend fun deleteAll()

}