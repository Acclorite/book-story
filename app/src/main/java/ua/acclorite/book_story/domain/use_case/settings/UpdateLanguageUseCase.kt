/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.domain.use_case.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import ua.acclorite.book_story.core.language.Language
import ua.acclorite.book_story.data.settings.SettingsManager
import javax.inject.Inject

class UpdateLanguageUseCase @Inject constructor(
    private val settings: SettingsManager
) {

    operator fun invoke(language: Language) {
        val appLocale = LocaleListCompat.forLanguageTags(language.locale.toLanguageTag())
        AppCompatDelegate.setApplicationLocales(appLocale)
        settings.language.update(language)
    }
}