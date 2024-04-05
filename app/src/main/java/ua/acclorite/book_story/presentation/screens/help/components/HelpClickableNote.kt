package ua.acclorite.book_story.presentation.screens.help.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CustomTooltip
import ua.acclorite.book_story.presentation.ui.SlidingTransition

/**
 * When clicked returns a Tag from [tags] (if present in [text])
 */
@Composable
fun LazyItemScope.HelpClickableNote(
    text: AnnotatedString,
    tags: List<String> = emptyList(),
    isNoteFullyShown: Boolean,
    onIconClick: () -> Unit,
    onTagClick: (String) -> Unit
) {
    Column(Modifier.animateItem()) {
        CustomTooltip(text = stringResource(R.string.note_content_desc)) {
            Icon(
                imageVector = Icons.Outlined.Info,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(interactionSource = null, indication = null) {
                        onIconClick()
                    },
                contentDescription = stringResource(R.string.note_content_desc),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        SlidingTransition(visible = isNoteFullyShown) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                ClickableText(
                    text = text,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) { offset ->
                    tags.forEach { tag ->
                        text.getStringAnnotations(tag = tag, start = offset, end = offset)
                            .firstOrNull()?.let {
                                onTagClick(tag)
                            }
                    }
                }
            }
        }
    }
}






