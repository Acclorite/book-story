package ua.acclorite.book_story.ui.about

import android.content.Context
import androidx.compose.runtime.Immutable

@Immutable
sealed class AboutEvent {
    data class OnNavigateToBrowserPage(
        val page: String,
        val context: Context
    ) : AboutEvent()

    data class OnCheckForUpdate(
        val context: Context
    ) : AboutEvent()

    data object OnDismissDialog : AboutEvent()
}