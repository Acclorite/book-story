/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.local.notification

import ua.acclorite.book_story.data.remote.dto.LatestReleaseInfo

interface UpdatesNotificationService {

    fun postNotification(releaseInfo: LatestReleaseInfo)

    companion object {
        const val CHANNEL_ID = "updates_channel"
    }
}