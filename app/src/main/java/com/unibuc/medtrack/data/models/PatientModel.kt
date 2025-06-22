package com.unibuc.medtrack.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "patients")
data class PatientModel(
    @PrimaryKey
    val id: String,
    val userId: String,
    val dateOfBirth: String,
    val phoneNumber: String
)

