/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.settings

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.core.language.Language
import ua.acclorite.book_story.domain.model.library.Category

@Immutable
sealed class SettingsEvent {
    data class OnUpdateLanguage(
        val language: Language
    ) : SettingsEvent()

    data class OnGrantPersistableUriPermission(
        val uri: String
    ) : SettingsEvent()

    data class OnReleasePersistableUriPermission(
        val uri: String
    ) : SettingsEvent()

    data class OnCreateCategory(
        val title: String
    ) : SettingsEvent()

    data class OnUpdateCategory(
        val category: Category
    ) : SettingsEvent()

    data class OnUpdateCategoryOrder(
        val categories: List<Category>
    ) : SettingsEvent()

    data class OnRemoveCategory(
        val category: Category
    ) : SettingsEvent()

    data class OnSelectColorPreset(
        val id: Int,
    ) : SettingsEvent()

    data class OnSwitchColorPreset(
        val previous: Boolean
    ) : SettingsEvent()

    data class OnDeleteColorPreset(
        val id: Int
    ) : SettingsEvent()

    data class OnUpdateColorPresetTitle(
        val id: Int,
        val title: String
    ) : SettingsEvent()

    data class OnShuffleColorPreset(
        val id: Int
    ) : SettingsEvent()

    data class OnAddColorPreset(
        val backgroundColor: Color,
        val fontColor: Color
    ) : SettingsEvent()

    data class OnUpdateColorPresetColor(
        val id: Int,
        val backgroundColor: Color?,
        val fontColor: Color?
    ) : SettingsEvent()

    data class OnReorderColorPresets(
        val from: Int,
        val to: Int
    ) : SettingsEvent()

    data object OnConfirmReorderColorPresets : SettingsEvent()
}