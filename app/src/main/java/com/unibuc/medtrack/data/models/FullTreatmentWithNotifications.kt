package com.unibuc.medtrack.data.models

import androidx.room.Embedded
import androidx.room.Relation

data class FullTreatmentWithNotifications (
    @Embedded val notification: NotificationModel,

    @Relation(
        parentColumn = "treatmentId",
        entityColumn = "id"
    )
    val treatment: TreatmentModel
)