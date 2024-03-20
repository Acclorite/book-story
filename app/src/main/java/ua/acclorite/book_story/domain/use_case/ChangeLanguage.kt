package ua.acclorite.book_story.domain.use_case

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.domain.util.DataStoreConstants
import javax.inject.Inject

class ChangeLanguage @Inject constructor(private val repository: BookRepository) {

    suspend fun execute(language: String) {
        val appLocale = LocaleListCompat.forLanguageTags(language)
        AppCompatDelegate.setApplicationLocales(appLocale)

        repository.putDataToDataStore(
            DataStoreConstants.LANGUAGE,
            language
        )
    }
}