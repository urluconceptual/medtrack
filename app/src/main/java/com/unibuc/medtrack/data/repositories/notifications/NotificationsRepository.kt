package com.unibuc.medtrack.data.repositories.notifications

import com.unibuc.medtrack.data.models.FullTreatmentWithNotifications
import com.unibuc.medtrack.data.models.NotificationModel
import java.time.LocalDateTime
import java.util.UUID

interface NotificationsRepository {
    suspend fun getAll(): List<NotificationModel>
    suspend fun getById(id: UUID): NotificationModel?
    suspend fun getByTreatment(treatmentId: String): List<NotificationModel>
    suspend fun insert(notification: NotificationModel)
    suspend fun insertAll(notifications: List<NotificationModel>)
    suspend fun update(notification: NotificationModel)
    suspend fun delete(notification: NotificationModel)
    suspend fun getTodayNotificationsWithTreatment(patientId: String, date: LocalDateTime): List<FullTreatmentWithNotifications>
}