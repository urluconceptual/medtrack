package com.unibuc.medtrack.data.models

enum class UserType {
    DOCTOR,
    PATIENT;

    companion object {
        fun fromString(value: String): UserType {
            return when (value) {
                "DOCTOR" -> DOCTOR
                "PATIENT" -> PATIENT
                else -> throw IllegalArgumentException("Unknown user type: $value")
            }
        }
    }
}