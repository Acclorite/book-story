/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.domain.use_case.book

import androidx.lifecycle.ViewModel
import ua.blindmint.codex.domain.library.book.SyncStatus
import ua.blindmint.codex.domain.repository.BookRepository
import javax.inject.Inject

data class SyncStatistics(
    val syncedCount: Int = 0,
    val syncingCount: Int = 0,
    val errorCount: Int = 0
)

class GetSyncStatisticsUseCase @Inject constructor(
    private val bookRepository: BookRepository
) : ViewModel() {

    suspend fun execute(): SyncStatistics {
        val books = bookRepository.getBooks("")
        val syncedCount = books.count { it.syncStatus == SyncStatus.SYNCED }
        val syncingCount = books.count { it.syncStatus == SyncStatus.SYNCING }
        val errorCount = books.count { it.syncStatus == SyncStatus.SYNC_ERROR }

        return SyncStatistics(
            syncedCount = syncedCount,
            syncingCount = syncingCount,
            errorCount = errorCount
        )
    }
}