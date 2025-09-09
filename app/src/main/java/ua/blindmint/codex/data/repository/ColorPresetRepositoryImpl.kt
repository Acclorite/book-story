/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.data.repository

import ua.blindmint.codex.data.local.room.BookDao
import ua.blindmint.codex.data.mapper.color_preset.ColorPresetMapper
import ua.blindmint.codex.domain.reader.ColorPreset
import ua.blindmint.codex.domain.repository.ColorPresetRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Color Preset repository.
 * Manages all [ColorPreset] related work.
 */
@Singleton
class ColorPresetRepositoryImpl @Inject constructor(
    private val database: BookDao,

    private val colorPresetMapper: ColorPresetMapper
) : ColorPresetRepository {

    /**
     * Update color preset.
     */
    override suspend fun updateColorPreset(colorPreset: ColorPreset) {
        database.updateColorPreset(
            colorPresetMapper.toColorPresetEntity(
                colorPreset,
                if (colorPreset.id != -1) database.getColorPresetOrder(colorPreset.id)
                else database.getColorPresetsSize()
            )
        )
    }

    /**
     * Select color preset. Only one can be selected at time.
     */
    override suspend fun selectColorPreset(colorPreset: ColorPreset) {
        database.getColorPresets().map {
            it.copy(
                isSelected = it.id == colorPreset.id
            )
        }.forEach {
            database.updateColorPreset(it)
        }
    }

    /**
     * Get all color presets.
     * Sorted by order (either manual or newest ones at the end).
     */
    override suspend fun getColorPresets(): List<ColorPreset> {
        return database.getColorPresets()
            .sortedBy { it.order }
            .map { colorPresetMapper.toColorPreset(it) }
    }

    /**
     * Reorder color presets.
     * Changes the order of the color presets.
     */
    override suspend fun reorderColorPresets(orderedColorPresets: List<ColorPreset>) {
        database.deleteColorPresets()

        orderedColorPresets.forEachIndexed { index, colorPreset ->
            database.updateColorPreset(
                colorPresetMapper.toColorPresetEntity(colorPreset, order = index)
            )
        }
    }

    /**
     * Delete color preset.
     */
    override suspend fun deleteColorPreset(colorPreset: ColorPreset) {
        database.deleteColorPreset(
            colorPresetMapper.toColorPresetEntity(
                colorPreset, -1
            )
        )
    }
}