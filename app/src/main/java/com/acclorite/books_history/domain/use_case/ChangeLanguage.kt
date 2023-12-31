package com.acclorite.books_history.domain.use_case

import androidx.activity.ComponentActivity
import com.acclorite.books_history.domain.repository.BookRepository
import com.acclorite.books_history.util.DataStoreConstants
import java.util.Locale
import javax.inject.Inject

class ChangeLanguage @Inject constructor(private val repository: BookRepository) {
    suspend fun execute(language: String, activity: ComponentActivity) {
        val config = activity.resources.configuration
        val resources = activity.resources
        val locale = Locale(language)

        Locale.setDefault(locale)
        config.setLocale(locale)

        activity.createConfigurationContext(config)
        resources.updateConfiguration(
            config,
            resources.displayMetrics
        )

        repository.putDataToDataStore(
            DataStoreConstants.LANGUAGE,
            language
        )
    }
}