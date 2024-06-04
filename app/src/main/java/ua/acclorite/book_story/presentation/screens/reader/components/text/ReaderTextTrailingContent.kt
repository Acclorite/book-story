package ua.acclorite.book_story.presentation.screens.reader.components.text

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.TextUnit
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.LineWithTranslation
import ua.acclorite.book_story.presentation.ui.DefaultTransition

/**
 * [ReaderTextParagraph]'s trailing content.
 *
 * @param line [ReaderTextParagraph]'s line.
 * @param color Color.
 * @param textSize Font size.
 * @param context Context.
 * @param density [Density] from LocalDensity.current.
 */
@Composable
fun readerTextTrailingContent(
    line: LineWithTranslation,
    color: Color,
    textSize: TextUnit,
    context: Context,
    density: Density
): Map<String, InlineTextContent> {
    val sizeDp = remember(textSize, density) { with(density) { textSize.toDp() } }
    return remember(line.isTranslationLoading, line.isTranslationFailed, sizeDp, color, textSize) {
        mapOf(
            Pair(
                "translator",
                InlineTextContent(
                    placeholder = Placeholder(
                        width = textSize,
                        height = textSize,
                        placeholderVerticalAlign = PlaceholderVerticalAlign.Center
                    ),
                ) {
                    Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        DefaultTransition(line.isTranslationLoading) {
                            CircularProgressIndicator(
                                strokeWidth = sizeDp / 12,
                                modifier = Modifier
                                    .fillMaxSize(0.7f),
                                color = color
                            )
                        }

                        DefaultTransition(line.isTranslationFailed && !line.isTranslationLoading) {
                            Icon(
                                imageVector = Icons.Filled.Error,
                                contentDescription = stringResource(id = R.string.error_content_desc),
                                tint = color,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable(
                                        interactionSource = null,
                                        indication = null
                                    ) {
                                        line.errorMessage?.let {
                                            Toast
                                                .makeText(
                                                    context,
                                                    it.asString(context),
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }
                            )
                        }
                    }
                }
            )
        )
    }
}