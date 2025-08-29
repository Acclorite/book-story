/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.history

import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.domain.model.history.History
import ua.acclorite.book_story.domain.repository.HistoryRepository
import ua.acclorite.book_story.presentation.history.model.GroupedHistory
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetHistoryUseCase @Inject constructor(
    private val historyRepository: HistoryRepository
) {

    suspend operator fun invoke(query: String): List<GroupedHistory> {
        logI("Getting all history.")

        fun getDayLabel(timeMillis: Long): String {
            val historyDate = Instant.ofEpochMilli(timeMillis)
                .atZone(ZoneId.systemDefault()).toLocalDate()
            val today = LocalDate.now()
            val yesterday = today.minusDays(1)

            return when (historyDate) {
                today -> "today"
                yesterday -> "yesterday"
                else -> historyDate.format(DateTimeFormatter.ofPattern("dd.MM.yy"))
            }
        }

        fun filterMaxElementsById(elements: List<History>): List<History> {
            val groupedById = elements.groupBy { it.book.id }
            val maxElementsById = groupedById.map { (_, values) ->
                values.maxByOrNull { it.time }
            }
            return maxElementsById.filterNotNull()
        }

        val query = query.lowercase().trim()
        return runCatching {
            historyRepository.getHistory().getOrThrow()
                .filter { history -> history.book.title.lowercase().trim().contains(query) }
                .sortedByDescending { history -> history.time }
                .groupBy { history -> getDayLabel(history.time) }
                .map { (day, history) -> GroupedHistory(day, filterMaxElementsById(history)) }
        }.fold(
            onSuccess = {
                logI("Successfully got ${it.size} grouped history entries.")
                it
            },
            onFailure = {
                logE("Could not get grouped history entries with error: ${it.message}")
                emptyList()
            }
        )
    }
}