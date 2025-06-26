package com.unibuc.medtrack.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity(
    tableName = "treatments"
)
data class TreatmentModel(
    @PrimaryKey val id: UUID,
    val doctorId: String,
    val patientId: String,
    val startDate: String,
    val endDate: String,
    val createdAt: LocalDateTime,
    val medicineName: String,
    val description: String,
    val dose_interval: Int,
    val dosage: Double,
    val dosageUnit: MeasurementUnit
){
    override fun toString(): String {
        return "TreatmentModel(id=$id, doctorId='$doctorId', patientId='$patientId', startDate='$startDate', endDate='$endDate', createdAt=$createdAt, medicineName='$medicineName', description='$description', dose_interval=$dose_interval, dosage=$dosage, dosageUnit=$dosageUnit)"
    }
}