package ua.acclorite.book_story.presentation.screens.help.components.items

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.screens.help.components.HelpAnnotation
import ua.acclorite.book_story.presentation.screens.help.components.HelpClickableNote
import ua.acclorite.book_story.presentation.screens.help.data.HelpEvent
import ua.acclorite.book_story.presentation.screens.help.data.HelpState

@Composable
fun LazyItemScope.HelpClickMeNoteItem(
    state: State<HelpState>,
    onEvent: (HelpEvent) -> Unit
) {
    val context = LocalContext.current

    HelpClickableNote(
        text = buildAnnotatedString {
            append(stringResource(id = R.string.help_clickable_note_1) + " ")

            HelpAnnotation(tag = "note") {
                append(stringResource(id = R.string.help_clickable_note_2))
            }

            append(" " + stringResource(id = R.string.help_clickable_note_3))
        },
        tags = listOf("note"),
        isNoteFullyShown = state.value.showNote1,
        onIconClick = {
            onEvent(
                HelpEvent.OnUpdateState {
                    it.copy(
                        showNote1 = !it.showNote1
                    )
                }
            )
        },
        onTagClick = { tag ->
            when (tag) {
                "note" -> {
                    Toast.makeText(
                        context,
                        context.getString(R.string.help_clickable_note_action),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    )
}