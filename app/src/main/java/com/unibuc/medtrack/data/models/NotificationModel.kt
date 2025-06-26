package com.unibuc.medtrack.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime
import java.util.UUID

@Entity(tableName = "notifications")
class NotificationModel (
    @PrimaryKey
    val id: String,
    val treatmentId: String,
    val time: LocalDateTime,
    val takenAt: LocalDateTime?
){
    override fun toString(): String {
        return "NotificationModel(id=$id, treatmentId='$treatmentId', time=$time, takenAt=$takenAt)"
    }
}