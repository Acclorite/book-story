package ua.acclorite.book_story.presentation.screens.help.components.items

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.util.showToast
import ua.acclorite.book_story.presentation.screens.help.components.HelpAnnotation
import ua.acclorite.book_story.presentation.screens.help.components.HelpClickableNote

/**
 * Help Click Me Note Item.
 * Shows an example to user on how to interact with highlighted text.
 */
@Composable
fun HelpClickMeNoteItem() {
    val context = LocalContext.current

    HelpClickableNote(
        text = buildAnnotatedString {
            append(stringResource(id = R.string.help_clickable_note_1) + " ")

            HelpAnnotation(
                onClick = {
                    context.getString(R.string.help_clickable_note_action)
                        .showToast(context = context, longToast = false)
                }
            ) {
                append(stringResource(id = R.string.help_clickable_note_2))
            }

            append(" " + stringResource(id = R.string.help_clickable_note_3))
        }
    )
}