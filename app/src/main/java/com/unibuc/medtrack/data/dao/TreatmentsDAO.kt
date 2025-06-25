package com.unibuc.medtrack.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.unibuc.medtrack.data.models.TreatmentModel
import java.util.UUID

@Dao
interface TreatmentsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(treatment: TreatmentModel)

    @Query("SELECT * FROM treatments WHERE id = :id")
    suspend fun getById(id: UUID): TreatmentModel?

    @Query("SELECT * FROM treatments WHERE patientId = :patientId")
    suspend fun getByPatientId(patientId: String): List<TreatmentModel>

    @Query("SELECT * FROM treatments WHERE doctorId = :doctorId")
    suspend fun getByDoctorId(doctorId: String): List<TreatmentModel>

    @Query("SELECT * FROM treatments")
    suspend fun getAll(): List<TreatmentModel>
}