/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.mapper.color_preset

import ua.acclorite.book_story.data.local.dto.ColorPresetEntity
import ua.acclorite.book_story.domain.model.reader.ColorPreset

interface ColorPresetMapper {
    fun toColorPresetEntity(colorPreset: ColorPreset, order: Int): ColorPresetEntity
    fun toColorPreset(colorPresetEntity: ColorPresetEntity): ColorPreset
}