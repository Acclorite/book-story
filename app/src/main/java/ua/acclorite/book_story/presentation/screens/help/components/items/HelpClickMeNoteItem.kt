package ua.acclorite.book_story.presentation.screens.help.components.items

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.screens.help.components.HelpAnnotation
import ua.acclorite.book_story.presentation.screens.help.components.HelpClickableNote

/**
 * Help Click Me Note Item.
 * Shows an example to user on how to interact with highlighted text.
 */
@Composable
fun LazyItemScope.HelpClickMeNoteItem() {
    val context = LocalContext.current

    HelpClickableNote(
        text = buildAnnotatedString {
            append(stringResource(id = R.string.help_clickable_note_1) + " ")

            HelpAnnotation(
                onClick = {
                    Toast.makeText(
                        context,
                        context.getString(R.string.help_clickable_note_action),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            ) {
                append(stringResource(id = R.string.help_clickable_note_2))
            }

            append(" " + stringResource(id = R.string.help_clickable_note_3))
        }
    )
}