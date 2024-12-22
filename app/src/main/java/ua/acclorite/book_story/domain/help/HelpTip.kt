package ua.acclorite.book_story.domain.help

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString

@Immutable
data class HelpTip(
    @StringRes val title: Int,
    val description: @Composable AnnotatedString.Builder.() -> Unit
)