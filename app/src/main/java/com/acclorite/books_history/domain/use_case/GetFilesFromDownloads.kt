package com.acclorite.books_history.domain.use_case

import com.acclorite.books_history.domain.repository.BookRepository
import com.acclorite.books_history.util.Resource
import kotlinx.coroutines.flow.Flow
import java.io.File
import javax.inject.Inject

class GetFilesFromDownloads @Inject constructor(
    private val repository: BookRepository
) {

    suspend fun execute(query: String): Flow<Resource<List<File>>> {
        return repository.getFilesFromDownloads(query)
    }
}