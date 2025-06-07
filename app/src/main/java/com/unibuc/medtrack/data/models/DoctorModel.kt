package com.unibuc.medtrack.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "doctors")
data class DoctorModel(
    @PrimaryKey
    val id: String,
    val userId: UUID,
    val specialty: String
)