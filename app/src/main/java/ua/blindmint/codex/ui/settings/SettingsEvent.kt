/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.ui.settings

import android.content.Context
import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import ua.blindmint.codex.domain.util.ID

@Immutable
sealed class SettingsEvent {
    data class OnGrantPersistableUriPermission(
        val uri: Uri
    ) : SettingsEvent()

    data class OnReleasePersistableUriPermission(
        val uri: Uri
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