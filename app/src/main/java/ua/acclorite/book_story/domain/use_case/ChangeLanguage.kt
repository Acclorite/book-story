package ua.acclorite.book_story.domain.use_case

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import ua.acclorite.book_story.domain.repository.BookRepository
import ua.acclorite.book_story.util.DataStoreConstants
import java.util.Locale
import javax.inject.Inject

class ChangeLanguage @Inject constructor(private val repository: BookRepository) {
    @SuppressLint("AppBundleLocaleChanges")
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