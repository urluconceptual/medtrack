package com.unibuc.medtrack.data.worker

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.unibuc.medtrack.R

class NotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val treatmentName = inputData.getString("treatmentName") ?: return Result.failure()
        val dosage = inputData.getString("dosage") ?: "N/A"

        showNotification(treatmentName, dosage)

        return Result.success()
    }

    @SuppressLint("NotificationPermission")
    private fun showNotification(title: String, message: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "medtrack_notifications"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "MedTrack Notifications",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setSmallIcon(R.drawable.rounded_avg_time_24)
            .setContentTitle("MedTrack")
            .setContentText("It's time to take: $title - $message")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .build()

        notificationManager.notify((System.currentTimeMillis() % Int.MAX_VALUE).toInt(), notification)
    }
}
