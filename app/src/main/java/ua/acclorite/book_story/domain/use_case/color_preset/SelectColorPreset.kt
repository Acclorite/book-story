package ua.acclorite.book_story.domain.use_case.color_preset

import ua.acclorite.book_story.domain.reader.ColorPreset
import ua.acclorite.book_story.domain.repository.ColorPresetRepository
import javax.inject.Inject

class SelectColorPreset @Inject constructor(
    private val repository: ColorPresetRepository
) {

    suspend fun execute(colorPreset: ColorPreset) {
        repository.selectColorPreset(colorPreset)
    }
}