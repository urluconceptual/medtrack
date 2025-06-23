package com.unibuc.medtrack.data.repositories.patients

import com.unibuc.medtrack.data.dao.PatientsDAO
import com.unibuc.medtrack.data.models.PatientModel
import java.util.UUID

class PatientsRepositoryLocal(private val dao: PatientsDAO) : PatientsRepository {
    override suspend fun insert(patient: PatientModel) = dao.insert(patient)
    override suspend fun getPatientById(id: String) = dao.getPatientById(id)
    override suspend fun getPatientByUserId(userId: String) = dao.getPatientByUserId(userId)
    override suspend fun getAllPatients() = dao.getAllPatients()
} 