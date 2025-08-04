/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.settings

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import ua.acclorite.book_story.core.ID
import ua.acclorite.book_story.core.language.Language
import ua.acclorite.book_story.domain.model.library.Category
import ua.acclorite.book_story.presentation.library.model.LibrarySortOrder

@Immutable
sealed class SettingsEvent {
    data class OnUpdateLanguage(
        val language: Language
    ) : SettingsEvent()

    data class OnGrantPersistableUriPermission(
        val uri: Uri
    ) : SettingsEvent()

    data class OnReleasePersistableUriPermission(
        val uri: Uri
    ) : SettingsEvent()

    data class OnCreateCategory(
        val title: String
    ) : SettingsEvent()

    data class OnUpdateCategoryTitle(
        val id: Int,
        val title: String
    ) : SettingsEvent()

    data class OnUpdateCategoryOrder(
        val categories: List<Category>
    ) : SettingsEvent()

    data class OnRemoveCategory(
        val category: Category
    ) : SettingsEvent()

    data class OnUpdateCategorySort(
        val categoryId: Int,
        val sortOrder: LibrarySortOrder,
        val sortOrderDescending: Boolean
    ) : SettingsEvent()

    data class OnSelectColorPreset(
        val id: ID
    ) : SettingsEvent()

    data class OnSelectPreviousPreset(
        val context: Context
    ) : SettingsEvent()

    data class OnSelectNextPreset(
        val context: Context
    ) : SettingsEvent()

    data class OnDeleteColorPreset(
        val id: ID
    ) : SettingsEvent()

    data class OnUpdateColorPresetTitle(
        val id: ID,
        val title: String
    ) : SettingsEvent()

    data class OnShuffleColorPreset(
        val id: ID
    ) : SettingsEvent()

    data class OnAddColorPreset(
        val backgroundColor: Color,
        val fontColor: Color
    ) : SettingsEvent()

    data class OnReorderColorPresets(
        val from: Int,
        val to: Int
    ) : SettingsEvent()

    data class OnUpdateColorPresetColor(
        val id: ID,
        val backgroundColor: Color?,
        val fontColor: Color?
    ) : SettingsEvent()

    data object OnConfirmReorderColorPresets : SettingsEvent()

    data class OnScrollToColorPreset(
        val index: Int
    ) : SettingsEvent()
}