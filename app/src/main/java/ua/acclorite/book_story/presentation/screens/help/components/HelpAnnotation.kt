package ua.acclorite.book_story.presentation.screens.help.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

/**
 * Help clickable annotation. Mainly used for highlighting screens.
 */
@Composable
fun AnnotatedString.Builder.HelpAnnotation(
    tag: String,
    block: @Composable AnnotatedString.Builder.() -> Unit
) {
    pushStringAnnotation(tag = tag, annotation = "")
    withStyle(
        style = SpanStyle(
            color = MaterialTheme.colorScheme.tertiary,
            fontWeight = FontWeight.Medium
        )
    ) {
        block()
    }
    pop()
}