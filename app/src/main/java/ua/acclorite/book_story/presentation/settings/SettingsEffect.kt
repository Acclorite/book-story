/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.settings

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.reader.ColorPreset

@Immutable
sealed class SettingsEffect {
    data class OnSwitchedColorPreset(
        val newColorPreset: ColorPreset
    ) : SettingsEffect()
}