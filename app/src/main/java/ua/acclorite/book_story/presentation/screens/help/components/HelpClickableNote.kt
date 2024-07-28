package ua.acclorite.book_story.presentation.screens.help.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
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
import ua.acclorite.book_story.presentation.components.CustomTooltip
import ua.acclorite.book_story.presentation.ui.SlidingTransition

/**
 * Help Clickable Note.
 * Collapsible simple note to user.
 *
 * @param text [AnnotatedString].
 */
@Composable
fun LazyItemScope.HelpClickableNote(text: AnnotatedString) {
    val showNote = rememberSaveable { mutableStateOf(true) }

    Column(Modifier.animateItem()) {
        CustomTooltip(text = stringResource(R.string.note_content_desc)) {
            Icon(
                imageVector = Icons.Outlined.Info,
                modifier = Modifier
                    .size(20.dp)
                    .clickable(interactionSource = null, indication = null) {
                        showNote.value = !showNote.value
                    },
                contentDescription = stringResource(R.string.note_content_desc),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        SlidingTransition(visible = showNote.value) {
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






