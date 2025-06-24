package com.unibuc.medtrack.data.models

data class DoctorUserDTO(
    val userId: String,
    val name: String,
    val specialty: String
) {
    companion object {
        fun fromModel(userModel: UserModel, doctorModel: DoctorModel): DoctorUserDTO {
            return DoctorUserDTO(
                userId = doctorModel.userId,
                name = userModel.name,
                specialty = doctorModel.specialty
            )
        }
    }
}