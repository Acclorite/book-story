package ua.acclorite.book_story.presentation.screens.about.nested.license_info.data

import android.content.Context
import androidx.compose.runtime.Immutable

@Immutable
sealed class LicenseInfoEvent {
    data class OnOpenLicensePage(
        val page: String,
        val context: Context,
        val noAppsFound: () -> Unit
    ) : LicenseInfoEvent()
}