/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.helpers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

suspend fun <T, R> Iterable<T>.concurrentMap(
    concurrencyLimit: Int = 3,
    transform: suspend (T) -> R
): List<R> {
    return withContext(Dispatchers.IO) {
        chunked(concurrencyLimit).flatMap { items ->
            items.map { item ->
                async {
                    transform(item)
                }
            }.awaitAll()
        }
    }
}