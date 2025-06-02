/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.repository

import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.data.mapper.history.HistoryMapper
import ua.acclorite.book_story.domain.history.History
import ua.acclorite.book_story.domain.repository.HistoryRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HistoryRepositoryImpl @Inject constructor(
    private val database: BookDao,
    private val historyMapper: HistoryMapper
) : HistoryRepository {

    override suspend fun getHistoryForBook(bookId: Int): Result<History> = runCatching {
        database.getHistoryForBook(bookId).let {
            if (it == null) throw NoSuchElementException("Could not get history from [$bookId].")
            else historyMapper.toHistory(it)
        }
    }

    override suspend fun addHistory(history: History): Result<Unit> = runCatching {
        database.insertHistory(historyMapper.toHistoryEntity(history))
    }

    override suspend fun getHistory(): Result<List<History>> = runCatching {
        database.getHistory().map { historyMapper.toHistory(it) }
    }

    override suspend fun deleteWholeHistory(): Result<Unit> = runCatching {
        database.deleteWholeHistory().also {
            if (it == 0) throw Exception("Could not delete whole history in database.")
        }
    }

    override suspend fun deleteHistoryForBook(bookId: Int): Result<Unit> = runCatching {
        database.deleteHistoryForBook(bookId = bookId).also {
            if (it == 0) throw Exception("Could not delete history for book [$bookId] in database.")
        }
    }

    override suspend fun deleteHistory(history: History): Result<Unit> = runCatching {
        database.deleteHistory(historyMapper.toHistoryEntity(history)).also {
            if (it == 0) throw Exception("Could not delete history in database.")
        }
    }
}