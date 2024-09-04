package ua.acclorite.book_story.domain.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.presentation.core.navigation.Screen

@Immutable
data class NavigationItem(
    val screen: Screen,
    @StringRes val title: Int,
    @StringRes val tooltip: Int,
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int
)