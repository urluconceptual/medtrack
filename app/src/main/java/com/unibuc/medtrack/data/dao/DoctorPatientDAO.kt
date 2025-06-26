package com.unibuc.medtrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.unibuc.medtrack.data.models.DoctorPatientModel

@Dao
interface DoctorPatientDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addDoctorPatient(relationship: DoctorPatientModel)

    @Query("SELECT patientId FROM doctor_patients WHERE doctorId = :doctorId")
    suspend fun getPatientIdsForDoctor(doctorId: String): List<String>

    @Query("SELECT doctorId FROM doctor_patients WHERE patientId = :patientId")
    suspend fun getDoctorIdsForPatient(patientId: String): List<String>
}