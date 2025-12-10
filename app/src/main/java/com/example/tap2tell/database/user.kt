package com.example.tap2tell.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class user(
    @PrimaryKey val firebaseuid: String,
    val name: String = "",
    val email: String,
    val role: String
)