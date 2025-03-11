/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.settings

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.reader.ColorPreset
import ua.acclorite.book_story.presentation.core.constants.provideDefaultColorPreset

@Immutable
data class SettingsState(
    val colorPresets: List<ColorPreset> = emptyList(),
    val selectedColorPreset: ColorPreset = provideDefaultColorPreset(),
    val animateColorPreset: Boolean = false,
    val colorPresetListState: LazyListState = LazyListState()
)