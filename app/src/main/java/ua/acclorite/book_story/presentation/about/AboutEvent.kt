/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.about

import androidx.compose.runtime.Immutable

@Immutable
sealed class AboutEvent {
    data class OnNavigateToBrowserPage(
        val page: String
    ) : AboutEvent()

    data object OnNavigateToLicenses : AboutEvent()

    data object OnNavigateToCredits : AboutEvent()

    data object OnNavigateBack : AboutEvent()
}