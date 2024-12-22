package ua.acclorite.book_story.domain.about

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.vector.ImageVector

@Immutable
data class Badge(
    val id: String,
    val imageVector: ImageVector?,
    @DrawableRes val drawable: Int?,
    @StringRes val contentDescription: Int,
    val url: String?
)