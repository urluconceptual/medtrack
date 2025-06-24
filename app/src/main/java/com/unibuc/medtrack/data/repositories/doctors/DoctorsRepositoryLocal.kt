package com.unibuc.medtrack.data.repositories.doctors

import com.unibuc.medtrack.data.dao.DoctorsDAO
import com.unibuc.medtrack.data.models.DoctorModel
import com.unibuc.medtrack.data.models.DoctorUserDTO
import java.util.UUID

class DoctorsRepositoryLocal(private val dao: DoctorsDAO) : DoctorsRepository {
    override suspend fun insert(doctor: DoctorModel) = dao.insert(doctor)
    override suspend fun getDoctorById(id: String) = dao.getDoctorById(id)
    override suspend fun getDoctorUserDtoById(id: String): DoctorUserDTO? = dao.getDoctorUserDtoById(id)
    override suspend fun getDoctorByUserId(userId: String) = dao.getDoctorByUserId(userId)
    override suspend fun getAllDoctors() = dao.getAllDoctors()
    override suspend fun getDoctorsBySpecialty(specialty: String) = dao.getDoctorsBySpecialty(specialty)
    override suspend fun updateDoctor(doctor: DoctorModel) {
        dao.update(doctor)
    }
} 