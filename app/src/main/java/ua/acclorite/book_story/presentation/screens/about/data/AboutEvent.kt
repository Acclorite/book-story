package ua.acclorite.book_story.presentation.screens.about.data

import android.content.Context
import androidx.compose.runtime.Immutable

@Immutable
sealed class AboutEvent {
    data class OnNavigateToBrowserPage(
        val page: String,
        val context: Context,
        val noAppsFound: () -> Unit
    ) : AboutEvent()

    data class OnCheckForUpdates(
        val context: Context,
        val noUpdatesFound: () -> Unit,
        val error: () -> Unit
    ) : AboutEvent()

    data object OnDismissUpdateDialog : AboutEvent()
}