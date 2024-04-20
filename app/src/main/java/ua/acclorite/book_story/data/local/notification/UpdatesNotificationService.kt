package ua.acclorite.book_story.data.local.notification

import ua.acclorite.book_story.data.remote.dto.ReleaseResponse

interface UpdatesNotificationService {

    fun postNotification(releaseInfo: ReleaseResponse)

    companion object {
        const val CHANNEL_ID = "updates_channel"
    }
}