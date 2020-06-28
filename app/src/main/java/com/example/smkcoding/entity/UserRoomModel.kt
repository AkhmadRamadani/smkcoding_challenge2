package com.example.smkcoding.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserRoomModel(
    var nama: String,
    var email: String,
    var password: String,
    var verified: String,
    var api_key: String,
    @PrimaryKey var id_user: String
) {
    constructor(): this("","","","","","")
}