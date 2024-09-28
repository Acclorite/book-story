package ua.acclorite.book_story

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import dagger.hilt.android.HiltAndroidApp
import ua.acclorite.book_story.data.local.notification.UpdatesNotificationService

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            UpdatesNotificationService.CHANNEL_ID,
            getString(R.string.updates_notification_channel),
            NotificationManager.IMPORTANCE_HIGH
        )

        val notificationManager = getSystemService(
            NOTIFICATION_SERVICE
        ) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}






