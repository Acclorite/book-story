/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.data_store

import androidx.datastore.preferences.core.Preferences
import ua.acclorite.book_story.core.logE
import ua.acclorite.book_story.core.logI
import ua.acclorite.book_story.domain.repository.DataStoreRepository
import javax.inject.Inject

class PutPreferenceUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    suspend operator fun <T> invoke(key: Preferences.Key<T>, value: T) {
        logI("Putting preference [${key.name}].")

        dataStoreRepository.putPreference(key, value).fold(
            onSuccess = {
                logI("Successfully put preference [${key.name}].")
            },
            onFailure = {
                logE("Could not put preference [${key.name}] with error: ${it.message}")
            }
        )
    }
}