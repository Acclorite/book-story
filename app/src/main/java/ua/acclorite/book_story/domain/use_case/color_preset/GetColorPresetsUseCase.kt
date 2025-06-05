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

class GetColorPresetsUseCase @Inject constructor(
    private val colorPresetRepository: ColorPresetRepository
) {

    suspend operator fun invoke(): List<ColorPreset> {
        logI("Getting color presets.")

        return colorPresetRepository.getColorPresets().fold(
            onSuccess = {
                logI("Successfully got [${it.size}] color presets.")
                it
            },
            onFailure = {
                logE("Could not get color presets with error: ${it.message}")
                emptyList()
            }
        )
    }
}