package ua.acclorite.book_story.domain.model

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.AnnotatedString
import ua.acclorite.book_story.domain.util.OnNavigate

@Immutable
data class HelpTip(
    @StringRes val title: Int,
    val description: @Composable AnnotatedString.Builder.(onNavigate: OnNavigate, fromStart: Boolean) -> Unit
)