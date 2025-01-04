package ua.acclorite.book_story.presentation.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.ui.UIText
import ua.acclorite.book_story.presentation.core.components.placeholder.ErrorPlaceholder
import ua.acclorite.book_story.presentation.core.util.LocalActivity
import ua.acclorite.book_story.ui.reader.ReaderEvent

@Composable
fun ReaderErrorPlaceholder(
    errorMessage: UIText,
    leave: (ReaderEvent.OnLeave) -> Unit,
    navigateBack: () -> Unit
) {
    val activity = LocalActivity.current
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
        contentAlignment = Alignment.Center
    ) {
        ErrorPlaceholder(
            errorMessage = errorMessage.asString(),
            icon = painterResource(id = R.drawable.error),
            actionTitle = stringResource(id = R.string.go_back),
            action = {
                leave(
                    ReaderEvent.OnLeave(
                        activity = activity,
                        navigate = {
                            navigateBack()
                        }
                    )
                )
            }
        )
    }
}