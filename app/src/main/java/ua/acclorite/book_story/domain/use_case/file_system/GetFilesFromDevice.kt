package ua.acclorite.book_story.domain.use_case.file_system

import ua.acclorite.book_story.domain.browse.SelectableFile
import ua.acclorite.book_story.domain.repository.FileSystemRepository
import javax.inject.Inject

class GetFilesFromDevice @Inject constructor(
    private val repository: FileSystemRepository
) {

    suspend fun execute(query: String): List<SelectableFile> {
        return repository.getFilesFromDevice(query)
    }
}