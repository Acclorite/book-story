package ua.acclorite.book_story.presentation.screens.about.data

import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.data.remote.dto.ReleaseResponse

@Immutable
data class AboutState(
    val showUpdateDialog: Boolean = false,
    val updateInfo: ReleaseResponse? = null
)
