package ua.acclorite.book_story.presentation.screens.about.data

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.data.remote.dto.LatestReleaseInfo

@Immutable
data class AboutState(
    val showUpdateDialog: Boolean = false,
    val alreadyCheckedForUpdates: Boolean = false,
    val updateLoading: Boolean = false,
    val updateInfo: LatestReleaseInfo? = null
)
