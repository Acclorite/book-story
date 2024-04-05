package ua.acclorite.book_story.presentation.screens.help.data

import android.content.Context

sealed class HelpEvent {
    data class OnSearchInWeb(
        val context: Context,
        val noAppsFound: () -> Unit
    ) : HelpEvent()

    data class OnNavigateToBrowserPage(
        val page: String,
        val context: Context,
        val noAppsFound: () -> Unit
    ) : HelpEvent()
}