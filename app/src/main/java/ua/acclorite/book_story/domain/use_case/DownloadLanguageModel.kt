package ua.acclorite.book_story.domain.use_case

import ua.acclorite.book_story.domain.repository.BookRepository
import javax.inject.Inject

class DownloadLanguageModel @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(
        languageCode: String,
        onCompleted: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        repository.downloadLanguageModel(
            languageCode = languageCode,
            onCompleted = onCompleted,
            onFailure = onFailure
        )
    }
}