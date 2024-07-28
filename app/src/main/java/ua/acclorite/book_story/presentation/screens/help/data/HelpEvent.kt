package ua.acclorite.book_story.presentation.screens.help.data

import android.content.Context
import androidx.compose.runtime.Immutable

@Immutable
sealed class HelpEvent {
    data class OnSearchInWeb(
        val page: String,
        val context: Context,
        val error: () -> Unit,
        val noAppsFound: () -> Unit
    ) : HelpEvent()

    data class OnNavigateToBrowserPage(
        val page: String,
        val context: Context,
        val noAppsFound: () -> Unit
    ) : HelpEvent()
}