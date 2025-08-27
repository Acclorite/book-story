/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.data.local.room.BookDatabase
import ua.acclorite.book_story.data.mapper.color_preset.ColorPresetMapper
import ua.acclorite.book_story.domain.model.reader.ColorPreset
import ua.acclorite.book_story.domain.repository.ColorPresetRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ColorPresetRepositoryImpl @Inject constructor(
    private val database: BookDatabase,
    private val colorPresetMapper: ColorPresetMapper
) : ColorPresetRepository {

    override suspend fun getColorPresets(): Result<List<ColorPreset>> = runCatching {
        withContext(Dispatchers.IO) {
            database.colorPresetDao.getColorPresets().map {
                colorPresetMapper.toColorPreset(it)
            }
        }
    }

    override suspend fun updateColorPreset(
        colorPreset: ColorPreset
    ): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            database.colorPresetDao.updateColorPreset(
                colorPresetMapper.toColorPresetEntity(
                    colorPreset = colorPreset,
                    order = if (colorPreset.id != -1) {
                        database.colorPresetDao.getColorPresetOrder(colorPreset.id)
                    } else database.colorPresetDao.getColorPresetsSize()
                )
            )
        }
    }

    override suspend fun selectColorPreset(
        colorPreset: ColorPreset
    ): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            database.colorPresetDao.getColorPresets().map {
                it.copy(isSelected = it.id == colorPreset.id)
            }.forEach {
                database.colorPresetDao.updateColorPreset(it)
            }
        }
    }

    override suspend fun reorderColorPresets(
        colorPresets: List<ColorPreset>
    ): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            database.colorPresetDao.deleteColorPresets()
            colorPresets.forEachIndexed { index, colorPreset ->
                database.colorPresetDao.updateColorPreset(
                    colorPresetMapper.toColorPresetEntity(colorPreset, order = index)
                )
            }
        }
    }

    override suspend fun deleteColorPreset(
        colorPreset: ColorPreset
    ): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            database.colorPresetDao.deleteColorPreset(
                colorPresetMapper.toColorPresetEntity(
                    colorPreset, -1
                )
            )
        }
    }
}