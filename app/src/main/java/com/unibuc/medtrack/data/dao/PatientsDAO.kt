package com.unibuc.medtrack.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.unibuc.medtrack.data.models.PatientModel
import java.util.UUID

@Dao
interface PatientsDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(patient: PatientModel)

    @Update
    suspend fun update(patient: PatientModel)

    @Delete
    suspend fun delete(patient: PatientModel)

    @Query("SELECT * FROM patients")
    suspend fun getAllPatients(): List<PatientModel>

    @Query("SELECT * FROM patients WHERE id = :patientId")
    suspend fun getPatientById(patientId: String): PatientModel?

    @Query("SELECT * FROM patients WHERE userId = :userId")
    suspend fun getPatientByUserId(userId: String): PatientModel?
} 