/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.data.mapper.color_preset

import androidx.compose.ui.graphics.Color
import ua.blindmint.codex.data.local.dto.ColorPresetEntity
import ua.blindmint.codex.domain.reader.ColorPreset
import javax.inject.Inject

class ColorPresetMapperImpl @Inject constructor() : ColorPresetMapper {
    override suspend fun toColorPresetEntity(
        colorPreset: ColorPreset,
        order: Int
    ): ColorPresetEntity {
        return ColorPresetEntity(
            id = if (colorPreset.id != -1) colorPreset.id
            else null,
            name = colorPreset.name,
            backgroundColor = colorPreset.backgroundColor.value.toLong(),
            fontColor = colorPreset.fontColor.value.toLong(),
            isSelected = colorPreset.isSelected,
            order = order
        )
    }

    override suspend fun toColorPreset(colorPresetEntity: ColorPresetEntity): ColorPreset {
        return ColorPreset(
            id = colorPresetEntity.id!!,
            name = colorPresetEntity.name,
            backgroundColor = Color(colorPresetEntity.backgroundColor.toULong()),
            fontColor = Color(colorPresetEntity.fontColor.toULong()),
            isSelected = colorPresetEntity.isSelected
        )
    }
}