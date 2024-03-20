package ua.acclorite.book_story.presentation.screens.about.data

import android.content.Context

sealed class AboutEvent {
    data class OnNavigateToBrowserPage(
        val page: String,
        val context: Context,
        val noAppsFound: () -> Unit
    ) : AboutEvent()
}