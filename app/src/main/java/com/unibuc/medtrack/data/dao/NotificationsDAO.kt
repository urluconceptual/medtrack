package com.unibuc.medtrack.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.unibuc.medtrack.data.models.FullTreatmentWithNotifications
import com.unibuc.medtrack.data.models.NotificationModel
import java.time.LocalDateTime
import java.util.UUID

@Dao
interface NotificationsDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<NotificationModel>)

    @Update
    suspend fun updateNotification(notification: NotificationModel)

    @Delete
    suspend fun deleteNotification(notification: NotificationModel)

    @Query("SELECT * FROM notifications WHERE treatmentId = :treatmentId")
    suspend fun getNotificationsForTreatment(treatmentId: String): List<NotificationModel>

    @Query("SELECT * FROM notifications WHERE id = :id")
    suspend fun getNotificationById(id: String): NotificationModel?

    @Query("SELECT * FROM notifications")
    suspend fun getAllNotifications(): List<NotificationModel>

    @Transaction
    @Query("""
        SELECT * FROM notifications 
        WHERE DATE(time) = DATE(:today) 
        AND treatmentId IN (
            SELECT id FROM treatments WHERE patientId = :patientId
        )
    """)
    suspend fun getTodayNotificationsWithTreatment(patientId: String, today: LocalDateTime): List<FullTreatmentWithNotifications>

    @Query("UPDATE notifications SET takenAt = :takenAt WHERE id = :notificationId AND :takenAt >= time")
    suspend fun updateTakenAt(notificationId: String, takenAt: LocalDateTime)

}