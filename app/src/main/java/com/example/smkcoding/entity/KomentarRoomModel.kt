package com.example.smkcoding.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "komentar")
data class KomentarRoomModel(
    var id_post: String,
    var id_user: String,
    var komentar: String,
    @PrimaryKey var id_komentar: String
){
    constructor(): this("","","","")
}