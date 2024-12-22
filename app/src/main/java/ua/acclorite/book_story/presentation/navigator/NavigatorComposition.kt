package ua.acclorite.book_story.presentation.navigator

import androidx.compose.runtime.compositionLocalOf
import ua.acclorite.book_story.domain.navigator.Navigator

val LocalNavigator = compositionLocalOf<Navigator> { error("Navigator was not passed.") }