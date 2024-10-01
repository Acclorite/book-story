package ua.acclorite.book_story.domain.use_case.color_preset

import ua.acclorite.book_story.domain.model.ColorPreset
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class SelectColorPreset @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(colorPreset: ColorPreset) {
        repository.selectColorPreset(colorPreset)
    }
}