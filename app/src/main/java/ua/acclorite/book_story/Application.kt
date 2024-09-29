package ua.acclorite.book_story

import android.app.Application
import androidx.core.app.NotificationChannelCompat
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.HiltAndroidApp
import ua.acclorite.book_story.data.local.notification.UpdatesNotificationService

@HiltAndroidApp
class Application : Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannelCompat.Builder(
            UpdatesNotificationService.CHANNEL_ID,
            NotificationManagerCompat.IMPORTANCE_HIGH
        )
            .setName(getString(R.string.updates_notification_channel))
            .build()

        NotificationManagerCompat.from(this).createNotificationChannel(channel)
    }
}
