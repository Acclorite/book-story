/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.repository

import ua.acclorite.book_story.domain.model.reader.ColorPreset

interface ColorPresetRepository {
    suspend fun getColorPresets(): Result<List<ColorPreset>>

    suspend fun updateColorPreset(
        colorPreset: ColorPreset
    ): Result<Unit>

    suspend fun selectColorPreset(
        colorPreset: ColorPreset
    ): Result<Unit>

    suspend fun reorderColorPresets(
        colorPresets: List<ColorPreset>
    ): Result<Unit>

    suspend fun deleteColorPreset(
        colorPreset: ColorPreset
    ): Result<Unit>
}