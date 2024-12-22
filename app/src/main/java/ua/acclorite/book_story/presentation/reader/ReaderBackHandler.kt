package ua.acclorite.book_story.presentation.reader

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.presentation.core.util.LocalActivity
import ua.acclorite.book_story.ui.reader.ReaderEvent

@Composable
fun ReaderBackHandler(
    leave: (ReaderEvent.OnLeave) -> Unit,
    navigateBack: () -> Unit
) {
    val activity = LocalActivity.current

    BackHandler {
        leave(
            ReaderEvent.OnLeave(
                activity = activity,
                navigate = {
                    navigateBack()
                }
            )
        )
    }
}