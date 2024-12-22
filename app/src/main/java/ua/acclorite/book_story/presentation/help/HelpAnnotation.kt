package ua.acclorite.book_story.presentation.help

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

@Composable
fun AnnotatedString.Builder.HelpAnnotation(
    block: @Composable AnnotatedString.Builder.() -> Unit
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