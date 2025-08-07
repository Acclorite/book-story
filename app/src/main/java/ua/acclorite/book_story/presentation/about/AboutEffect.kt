/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.about

import androidx.compose.runtime.Immutable

@Immutable
sealed class AboutEffect {
    data class OnNavigateToBrowserPage(
        val page: String
    ) : AboutEffect()

    data object OnNavigateToLicenses : AboutEffect()

    data object OnNavigateToCredits : AboutEffect()

    data object OnNavigateBack : AboutEffect()
}