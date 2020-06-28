package com.example.smkcoding.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.smkcoding.dao.HomeDataDao
import com.example.smkcoding.entity.HomeDataModel
import com.example.smkcoding.entity.KomentarRoomModel
import com.example.smkcoding.entity.SukaiRoomModel
import com.example.smkcoding.entity.UserRoomModel

@Database(entities = arrayOf(HomeDataModel::class, UserRoomModel::class, SukaiRoomModel::class, KomentarRoomModel::class),version = 1, exportSchema = false)
public abstract class PostDatabase: RoomDatabase() {

    abstract fun homeDataDao(): HomeDataDao

    companion object{
        @Volatile
        private var INSTANCE: PostDatabase? = null
        fun getDatabase(context: Context): PostDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null){
                return tempInstance
            }
            synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PostDatabase::class.java,
                    "post_room_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}