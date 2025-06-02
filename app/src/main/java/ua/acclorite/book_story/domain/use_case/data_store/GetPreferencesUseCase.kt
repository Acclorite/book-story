/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.data_store

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.core.logE
import ua.acclorite.book_story.core.logI
import ua.acclorite.book_story.core.logW
import ua.acclorite.book_story.domain.repository.DataStoreRepository
import ua.acclorite.book_story.presentation.main.MainState
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

class GetPreferencesUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    suspend operator fun invoke(): MainState {
        logI("Getting all preferences (MainState).")

        return withContext(Dispatchers.Default) {
            dataStoreRepository.getAllPreferences().mapCatching { keys ->
                logI("Got [${keys.size}] preference keys.")

                val preferences = ConcurrentHashMap<String, Any>()
                keys.map { key ->
                    async {
                        dataStoreRepository.getPreference(key).fold(
                            onSuccess = {
                                it?.let { preference ->
                                    preferences[key.name] = preference
                                }
                            },
                            onFailure = {
                                logW("Could not get [${key.name}] preference.")
                                preferences.remove(key.name)
                            }
                        )
                    }
                }.awaitAll()

                MainState.initialize(preferences)
            }.fold(
                onSuccess = {
                    logI("Successfully got all preferences (MainState).")
                    it
                },
                onFailure = {
                    logE("Could not get all preferences (MainState) with error: ${it.message}")
                    MainState()
                }
            )
        }
    }
}