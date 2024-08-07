package ua.acclorite.book_story.presentation.screens.help.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle

/**
 * Help clickable annotation.
 * Mainly used for highlighting screens.
 *
 * @param onClick OnClick callback, to, for example navigate to another screen.
 * @param block Lambda to append all text to be highlighted.
 */
@Composable
fun AnnotatedString.Builder.HelpAnnotation(
    onClick: () -> Unit,
    block: @Composable AnnotatedString.Builder.() -> Unit
) {
    withLink(
        LinkAnnotation.Url("", styles = null) {
            onClick()
        }
    ) {
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.tertiary,
                fontWeight = FontWeight.Medium
            )
        ) {
            block()
        }
    }
}