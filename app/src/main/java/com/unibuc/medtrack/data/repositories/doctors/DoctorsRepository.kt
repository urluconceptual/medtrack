package com.unibuc.medtrack.data.repositories.doctors

import com.unibuc.medtrack.data.models.DoctorModel
import java.util.UUID

interface DoctorsRepository {
    suspend fun insert(doctor: DoctorModel)
    suspend fun getDoctorById(id: String): DoctorModel?
    suspend fun getDoctorByUserId(userId: String): DoctorModel?
    suspend fun getAllDoctors(): List<DoctorModel>
    suspend fun getDoctorsBySpecialty(specialty: String): List<DoctorModel>
} 