package com.unibuc.medtrack.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserModel(
    @PrimaryKey
    val id: String,
    val email: String,
    val name: String,
    val password: String,
    val role: String,
)