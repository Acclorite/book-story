package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.model.SelectableFile
import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class GetFilesFromDevice @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(query: String): List<SelectableFile> {
        return repository.getFilesFromDevice(query)
    }
}