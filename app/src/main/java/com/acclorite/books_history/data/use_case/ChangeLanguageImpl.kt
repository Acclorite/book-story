package com.acclorite.books_history.data.use_case

import androidx.activity.ComponentActivity
import com.acclorite.books_history.domain.use_case.ChangeLanguage
import java.util.Locale
import javax.inject.Inject

class ChangeLanguageImpl @Inject constructor() : ChangeLanguage {
    override fun execute(language: String, activity: ComponentActivity) {
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
    }
}