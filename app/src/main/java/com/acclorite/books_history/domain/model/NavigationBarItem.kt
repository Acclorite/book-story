package com.acclorite.books_history.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.painter.Painter

@Immutable
data class NavigationBarItem(
    val title: String,
    val selectedIcon: Painter,
    val unselectedIcon: Painter
)