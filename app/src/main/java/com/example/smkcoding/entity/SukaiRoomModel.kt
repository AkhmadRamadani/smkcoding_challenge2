package com.example.smkcoding.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sukai")
data class SukaiRoomModel(
    var id_post: String,
    var id_user: String,
    @PrimaryKey var id_suka: String
) {
    constructor(): this("","","")
}