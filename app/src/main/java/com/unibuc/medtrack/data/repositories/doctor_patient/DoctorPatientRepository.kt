package com.unibuc.medtrack.data.repositories.doctor_patient

interface DoctorPatientRepository{
    suspend fun addDoctorPatient(doctorId: String, patientId: String)
    suspend fun getPatientIdsForDoctor(doctorId: String): List<String>
}