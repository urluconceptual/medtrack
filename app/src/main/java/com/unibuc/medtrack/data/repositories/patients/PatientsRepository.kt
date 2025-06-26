package com.unibuc.medtrack.data.repositories.patients

import com.unibuc.medtrack.data.models.PatientUserDTO
import com.unibuc.medtrack.data.models.PatientModel
import java.util.UUID

interface PatientsRepository {
    suspend fun insert(patient: PatientModel)
    suspend fun getPatientById(id: String): PatientModel?
    suspend fun getPatientByUserId(userId: String): PatientModel?
    suspend fun getPatientUserDtoById(id: String): PatientUserDTO?
    suspend fun getAllPatients(): List<PatientModel>
    suspend fun updatePatient(patient: PatientModel)
}