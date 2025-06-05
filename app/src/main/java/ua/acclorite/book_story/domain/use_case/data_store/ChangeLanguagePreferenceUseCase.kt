/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.data_store

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.core.log.logI
import ua.acclorite.book_story.domain.repository.DataStoreRepository
import ua.acclorite.book_story.presentation.main.data.DataStoreData
import javax.inject.Inject

class ChangeLanguagePreferenceUseCase @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) {

    suspend operator fun invoke(language: String) {
        logI("Changing language preference.")

        dataStoreRepository.putPreference(
            DataStoreData.LANGUAGE,
            language
        ).fold(
            onSuccess = {
                logI("Successfully changed language preference.")

                val appLocale = LocaleListCompat.forLanguageTags(language)
                AppCompatDelegate.setApplicationLocales(appLocale)
            },
            onFailure = {
                logE("Could not change language preference with error: ${it.message}")
            }
        )
    }
}