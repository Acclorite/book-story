/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.repository

import ua.acclorite.book_story.domain.model.history.History

interface HistoryRepository {
    suspend fun getHistoryForBook(
        bookId: Int
    ): Result<History>

    suspend fun addHistory(
        history: History
    ): Result<Unit>

    suspend fun getHistory(): Result<List<History>>

    suspend fun deleteWholeHistory(): Result<Unit>

    suspend fun deleteHistoryForBook(
        bookId: Int
    ): Result<Unit>

    suspend fun deleteHistory(
        history: History
    ): Result<Unit>
}