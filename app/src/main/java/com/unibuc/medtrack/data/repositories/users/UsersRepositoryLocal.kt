package com.unibuc.medtrack.data.repositories.users

import android.util.Log
import com.unibuc.medtrack.data.dao.DoctorsDAO
import com.unibuc.medtrack.data.dao.PatientsDAO
import com.unibuc.medtrack.data.dao.UsersDAO
import com.unibuc.medtrack.data.models.FullDoctorProfile
import com.unibuc.medtrack.data.models.FullPatientProfile
import com.unibuc.medtrack.data.models.UserModel
import com.unibuc.medtrack.data.models.UserType

class UsersRepositoryLocal(val dao: UsersDAO, val doctorDAO: DoctorsDAO, val patientsDAO: PatientsDAO): UsersRepository {
    override suspend fun insert(users: UserModel) = dao.insert(users)
    override suspend fun getAll(): List<UserModel> = dao.getAll()
    override suspend fun getByEmail(email: String): UserModel? = dao.getByEmail(email)
    override suspend fun checkEmailAndPassword(email: String, password: String): Boolean {
        return dao.checkEmailAndPassword(email, password) != null
    }
    override suspend fun getFullDoctorProfileByEmail(email: String): FullDoctorProfile? {
        val user = dao.getByEmail(email)
        Log.i("UsersRepositoryLocal", "User found: " + user)
        if (user?.role != UserType.DOCTOR) return null
        val doctor = doctorDAO.getDoctorByUserId(user.id)
        Log.i("UsersRepositoryLocal", "Doctor found: " + doctor)
        return if (doctor != null) FullDoctorProfile(user, doctor) else null
    }
    override suspend fun getFullPatientProfileByEmail(email: String): FullPatientProfile? {
        val user = dao.getByEmail(email)
        Log.i("UsersRepositoryLocal", "User found: " + user)
        if (user?.role != UserType.PATIENT) return null
        val patient = patientsDAO.getPatientByUserId(user.id)
        Log.i("UsersRepositoryLocal", "Patient found: " + patient)
        return if (patient != null) FullPatientProfile(user, patient) else null
    }
    override suspend fun updateUser(user: UserModel) {
        dao.update(user)
    }
}