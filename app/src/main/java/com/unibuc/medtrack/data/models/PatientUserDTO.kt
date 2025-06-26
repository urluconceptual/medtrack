package com.unibuc.medtrack.data.models

data class PatientUserDTO(
    val userId: String,
    val name: String,
    val dateOfBirth: String
) {
    companion object {
        fun fromModel(userModel: UserModel, patientModel: PatientModel): PatientUserDTO {
            return PatientUserDTO(
                userId = patientModel.userId,
                name = userModel.name,
                dateOfBirth = patientModel.dateOfBirth
            )
        }
    }
}