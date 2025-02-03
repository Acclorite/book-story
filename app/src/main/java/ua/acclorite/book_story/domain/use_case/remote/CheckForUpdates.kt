/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.remote

import ua.acclorite.book_story.data.remote.dto.LatestReleaseInfo
import ua.acclorite.book_story.domain.repository.RemoteRepository
import javax.inject.Inject

class CheckForUpdates @Inject constructor(
    private val repository: RemoteRepository
) {

    suspend fun execute(postNotification: Boolean): LatestReleaseInfo? {
        return repository.checkForUpdates(postNotification)
    }
}