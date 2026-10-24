/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.reader.model

import androidx.annotation.StringRes
import ua.acclorite.book_story.R

enum class ReaderLayout(@StringRes val title: Int) {
    CONTINUOUS(R.string.layout_continuous),
    PAGED(R.string.layout_paged)
}