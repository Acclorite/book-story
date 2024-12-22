package ua.acclorite.book_story.data.repository

import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.mapper.color_preset.ColorPresetMapper
import ua.acclorite.book_story.domain.reader.ColorPreset
import ua.acclorite.book_story.domain.repository.ColorPresetRepository
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