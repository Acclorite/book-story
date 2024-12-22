package ua.acclorite.book_story.data.mapper.color_preset

import ua.acclorite.book_story.data.local.dto.ColorPresetEntity
import ua.acclorite.book_story.domain.reader.ColorPreset

interface ColorPresetMapper {
    suspend fun toColorPresetEntity(colorPreset: ColorPreset, order: Int): ColorPresetEntity

    suspend fun toColorPreset(colorPresetEntity: ColorPresetEntity): ColorPreset
}