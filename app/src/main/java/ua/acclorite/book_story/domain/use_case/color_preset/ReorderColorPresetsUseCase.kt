/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.color_preset

import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.domain.model.reader.ColorPreset
import ua.acclorite.book_story.domain.repository.ColorPresetRepository
import javax.inject.Inject

private const val TAG = "ReorderPresets"

class ReorderColorPresetsUseCase @Inject constructor(
    private val colorPresetRepository: ColorPresetRepository
) {

    suspend operator fun invoke(colorPresets: List<ColorPreset>) {
        logI(TAG, "Reordering [${colorPresets.size}] color presets.")

        colorPresetRepository.reorderColorPresets(colorPresets).fold(
            onSuccess = {
                logI(TAG, "Successfully reordered [${colorPresets.size}] color presets.")
            },
            onFailure = {
                logE(
                    TAG,
                    "Could not reorder [${colorPresets.size}] color presets with error: ${it.message}"
                )
            }
        )
    }
}