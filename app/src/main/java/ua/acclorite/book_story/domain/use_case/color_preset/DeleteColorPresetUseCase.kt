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

class DeleteColorPresetUseCase @Inject constructor(
    private val colorPresetRepository: ColorPresetRepository
) {

    suspend operator fun invoke(colorPreset: ColorPreset) {
        logI("Deleting [${colorPreset.id}] color preset.")

        colorPresetRepository.deleteColorPreset(colorPreset).fold(
            onSuccess = {
                logI("Successfully deleted [${colorPreset.id}] color preset.")
            },
            onFailure = {
                logE("Could not delete [${colorPreset.id}] color preset with error: ${it.message}")
            }
        )
    }
}