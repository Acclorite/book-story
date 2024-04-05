package ua.acclorite.book_story.presentation.screens.help.components.items

import android.widget.Toast
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.screens.help.components.HelpAnnotation
import ua.acclorite.book_story.presentation.screens.help.components.HelpClickableNote
import ua.acclorite.book_story.presentation.screens.help.data.HelpViewModel

@Composable
fun LazyItemScope.HelpClickMeNoteItem(viewModel: HelpViewModel) {
    val state by viewModel.state.collectAsState()
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
        isNoteFullyShown = state.showNote1,
        onIconClick = {
            viewModel.onUpdate {
                it.copy(
                    showNote1 = !it.showNote1
                )
            }
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