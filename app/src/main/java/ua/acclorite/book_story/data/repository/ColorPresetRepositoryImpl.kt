/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.data.mapper.color_preset.ColorPresetMapper
import ua.acclorite.book_story.data.storage.room.AppDatabase
import ua.acclorite.book_story.domain.model.reader.ColorPreset
import ua.acclorite.book_story.domain.repository.ColorPresetRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ColorPresetRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val colorPresetMapper: ColorPresetMapper
) : ColorPresetRepository {

    override suspend fun getColorPresets(): Result<List<ColorPreset>> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.colorPresetDao.getColorPresets().map {
                colorPresetMapper.toColorPreset(it)
            }
        }
    }

    override suspend fun updateColorPreset(
        colorPreset: ColorPreset
    ): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.colorPresetDao.updateColorPreset(
                colorPresetMapper.toColorPresetEntity(
                    colorPreset = colorPreset,
                    order = if (colorPreset.id != -1) {
                        appDatabase.colorPresetDao.getColorPresetOrder(colorPreset.id)
                    } else appDatabase.colorPresetDao.getColorPresetsSize()
                )
            )
        }
    }

    override suspend fun selectColorPreset(
        colorPreset: ColorPreset
    ): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.colorPresetDao.getColorPresets().map {
                it.copy(isSelected = it.id == colorPreset.id)
            }.forEach {
                appDatabase.colorPresetDao.updateColorPreset(it)
            }
        }
    }

    override suspend fun reorderColorPresets(
        colorPresets: List<ColorPreset>
    ): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.colorPresetDao.deleteColorPresets()
            colorPresets.forEachIndexed { index, colorPreset ->
                appDatabase.colorPresetDao.updateColorPreset(
                    colorPresetMapper.toColorPresetEntity(colorPreset, order = index)
                )
            }
        }
    }

    override suspend fun deleteColorPreset(
        colorPreset: ColorPreset
    ): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.colorPresetDao.deleteColorPreset(
                colorPresetMapper.toColorPresetEntity(
                    colorPreset, -1
                )
            )
        }
    }
}