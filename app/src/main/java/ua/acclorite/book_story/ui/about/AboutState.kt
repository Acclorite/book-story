/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.about

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.data.remote.dto.LatestReleaseInfo
import ua.acclorite.book_story.domain.util.Dialog

@Immutable
data class AboutState(
    val dialog: Dialog? = null,
    val updateChecked: Boolean = false,
    val updateLoading: Boolean = false,
    val updateInfo: LatestReleaseInfo? = null
)