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

private const val TAG = "SelectColorPreset"

class SelectColorPresetUseCase @Inject constructor(
    private val colorPresetRepository: ColorPresetRepository
) {

    suspend operator fun invoke(colorPreset: ColorPreset) {
        logI(TAG, "Selecting [${colorPreset.id}] color preset.")

        colorPresetRepository.selectColorPreset(colorPreset).fold(
            onSuccess = {
                logI(TAG, "Successfully selected [${colorPreset.id}] color preset.")
            },
            onFailure = {
                logE(
                    TAG,
                    "Could not select [${colorPreset.id}] color preset with error: ${it.message}"
                )
            }
        )
    }
}