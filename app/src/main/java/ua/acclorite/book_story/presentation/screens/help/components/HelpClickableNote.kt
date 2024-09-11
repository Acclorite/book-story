package ua.acclorite.book_story.presentation.screens.help.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.CustomTooltip
import ua.acclorite.book_story.presentation.core.util.noRippleClickable
import ua.acclorite.book_story.presentation.ui.ExpandingTransition

/**
 * Help Clickable Note.
 * Collapsible simple note to user.
 *
 * @param text [AnnotatedString].
 */
@Composable
fun HelpClickableNote(text: AnnotatedString) {
    val showNote = rememberSaveable { mutableStateOf(true) }

    Column(Modifier.padding(horizontal = 18.dp)) {
        CustomTooltip(text = stringResource(R.string.note_content_desc)) {
            Icon(
                imageVector = Icons.Outlined.Info,
                modifier = Modifier
                    .size(20.dp)
                    .noRippleClickable {
                        showNote.value = !showNote.value
                    },
                contentDescription = stringResource(R.string.note_content_desc),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        ExpandingTransition(visible = showNote.value) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
            }
        }
    }
}