package com.unibuc.medtrack.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctors")
data class DoctorModel(
    @PrimaryKey
    val id: String,
    val userId: String,
    val specialty: String
)