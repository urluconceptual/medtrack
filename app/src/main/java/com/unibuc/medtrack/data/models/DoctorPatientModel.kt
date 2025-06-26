package com.unibuc.medtrack.data.models

import androidx.room.Entity

@Entity(tableName = "doctor_patients", primaryKeys = ["doctorId", "patientId"])
data class DoctorPatientModel(
    val doctorId: String,
    val patientId: String
)