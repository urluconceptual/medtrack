package com.unibuc.medtrack.data.repositories.doctor_patient

import com.unibuc.medtrack.data.dao.DoctorPatientDao
import com.unibuc.medtrack.data.models.DoctorPatientModel

class DoctorPatientRepositoryLocal(private val dao: DoctorPatientDao) : DoctorPatientRepository {
    override suspend fun addDoctorPatient(doctorId: String, patientId: String) {
        dao.addDoctorPatient(DoctorPatientModel(doctorId, patientId))
    }
    override suspend fun getPatientIdsForDoctor(doctorId: String): List<String> =
        dao.getPatientIdsForDoctor(doctorId)
}