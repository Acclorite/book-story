/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.data.mapper.history.HistoryMapper
import ua.acclorite.book_story.data.storage.room.AppDatabase
import ua.acclorite.book_story.domain.model.history.History
import ua.acclorite.book_story.domain.repository.HistoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepositoryImpl @Inject constructor(
    private val appDatabase: AppDatabase,
    private val historyMapper: HistoryMapper
) : HistoryRepository {

    override suspend fun getHistoryForBook(bookId: Int): Result<History> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.historyDao.getHistoryForBook(bookId).let {
                if (it == null) throw NoSuchElementException("Could not get history from [$bookId]")
                else historyMapper.toHistory(it)
            }
        }
    }

    override suspend fun addHistory(history: History): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.historyDao.insertHistory(historyMapper.toHistoryEntity(history))
        }
    }

    override suspend fun getHistory(): Result<List<History>> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.historyDao.getHistoryWithBook().map { historyMapper.toHistory(it) }
        }
    }

    override suspend fun deleteWholeHistory(): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.historyDao.deleteWholeHistory()
        }
    }

    override suspend fun deleteHistoryForBook(bookId: Int): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.historyDao.deleteHistoryForBook(bookId = bookId)
        }
    }

    override suspend fun deleteHistory(history: History): Result<Unit> = runCatching {
        withContext(Dispatchers.IO) {
            appDatabase.historyDao.deleteHistory(historyMapper.toHistoryEntity(history)).also {
                if (it == 0) throw Exception("Could not delete history in database")
            }
        }
    }
}
