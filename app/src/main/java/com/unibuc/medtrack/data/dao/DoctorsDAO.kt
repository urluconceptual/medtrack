package com.unibuc.medtrack.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.unibuc.medtrack.data.models.DoctorModel
import com.unibuc.medtrack.data.models.DoctorUserDTO
import java.util.UUID

@Dao
interface DoctorsDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(doctor: DoctorModel)

    @Update
    suspend fun update(doctor: DoctorModel)

    @Delete
    suspend fun delete(doctor: DoctorModel)

    @Query("SELECT * FROM doctors")
    suspend fun getAllDoctors(): List<DoctorModel>

    @Query("SELECT * FROM doctors WHERE id = :doctorId")
    suspend fun getDoctorById(doctorId: String): DoctorModel?

    @Query("""
        SELECT d.userId, d.specialty, u.name
            FROM doctors d
            JOIN users u
                ON d.userId = u.id
        WHERE u.id = :id""")
    suspend fun getDoctorUserDtoById(id: String): DoctorUserDTO?

    @Query("SELECT * FROM doctors WHERE userId = :userId")
    suspend fun getDoctorByUserId(userId: String): DoctorModel?

    @Query("SELECT * FROM doctors WHERE specialty = :specialty")
    suspend fun getDoctorsBySpecialty(specialty: String): List<DoctorModel>
}