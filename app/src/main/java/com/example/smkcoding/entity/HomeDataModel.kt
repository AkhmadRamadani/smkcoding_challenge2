package com.example.smkcoding.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post")
data class HomeDataModel(
    var email: String,
    var text: String,
    var img: String,
    var id_user: String,
    var isLiked: String,
    var nama: String,
    var totalKomen: String,
    var totalLike: String,
    @PrimaryKey var id_post: String
){
    constructor(): this("","","","","","","","","")
}