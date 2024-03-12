package ua.acclorite.book_story.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter

@Immutable
data class NavigationItem(
    val title: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter
)