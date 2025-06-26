package com.unibuc.medtrack.data.repositories.treatments

import com.unibuc.medtrack.data.dao.TreatmentsDAO
import com.unibuc.medtrack.data.models.TreatmentModel
import java.util.UUID

class TreatmentRepositoryLocal(private val dao: TreatmentsDAO): TreatmentsRepository {
    override suspend fun insert(treatment: TreatmentModel) = dao.insert(treatment)
    override suspend fun getById(id: UUID): TreatmentModel? = dao.getById(id)
    override suspend fun getByPatientId(patientId: String): List<TreatmentModel> = dao.getByPatientId(patientId)
    override suspend fun getByDoctorId(doctorId: String): List<TreatmentModel> = dao.getByDoctorId(doctorId)
    override suspend fun getAll(): List<TreatmentModel> = dao.getAll()

}