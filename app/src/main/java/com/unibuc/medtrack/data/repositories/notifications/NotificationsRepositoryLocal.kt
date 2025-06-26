package com.unibuc.medtrack.data.repositories.notifications

import android.util.Log
import com.unibuc.medtrack.data.dao.NotificationsDAO
import com.unibuc.medtrack.data.models.FullTreatmentWithNotifications
import com.unibuc.medtrack.data.models.NotificationModel
import java.time.LocalDateTime
import java.util.UUID

class NotificationsRepositoryLocal(
    private val dao: NotificationsDAO
) : NotificationsRepository {
    override suspend fun getAll(): List<NotificationModel> = dao.getAllNotifications()

    override suspend fun getById(id: UUID): NotificationModel? = dao.getNotificationById(id)

    override suspend fun getByTreatment(treatmentId: String): List<NotificationModel> =
        dao.getNotificationsForTreatment(treatmentId)

    override suspend fun insert(notification: NotificationModel) {
        dao.insertNotification(notification)
    }

    override suspend fun insertAll(notifications: List<NotificationModel>) {
        dao.insertAll(notifications)
    }

    override suspend fun update(notification: NotificationModel) {
        dao.updateNotification(notification)
    }

    override suspend fun delete(notification: NotificationModel) {
        dao.deleteNotification(notification)
    }

    override suspend fun getTodayNotificationsWithTreatment(patientId: String, date: LocalDateTime): List<FullTreatmentWithNotifications> {
        Log.i("NotificationsRepository", "data sent to query: " + patientId + " today: " + date);
        return dao.getTodayNotificationsWithTreatment(patientId, date)
    }

}