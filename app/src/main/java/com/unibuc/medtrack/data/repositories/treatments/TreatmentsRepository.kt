package com.unibuc.medtrack.data.repositories.treatments

import com.unibuc.medtrack.data.models.TreatmentModel
import java.util.UUID

interface TreatmentsRepository {
    suspend fun insert(treatment: TreatmentModel)
    suspend fun getById(id: UUID): TreatmentModel?
    suspend fun getByPatientId(patientId: String): List<TreatmentModel>
    suspend fun getByDoctorId(doctorId: String): List<TreatmentModel>
    suspend fun getAll(): List<TreatmentModel>
}