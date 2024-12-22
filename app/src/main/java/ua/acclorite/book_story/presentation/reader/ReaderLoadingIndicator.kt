package ua.acclorite.book_story.presentation.reader

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.ui.UIText
import ua.acclorite.book_story.presentation.core.components.placeholder.ErrorPlaceholder
import ua.acclorite.book_story.presentation.core.components.progress_indicator.CircularProgressIndicator
import ua.acclorite.book_story.presentation.core.util.LocalActivity
import ua.acclorite.book_story.ui.reader.ReaderEvent

@Composable
fun ReaderLoadingIndicator(
    isLoading: Boolean,
    errorMessage: UIText?,
    leave: (ReaderEvent.OnLeave) -> Unit,
    navigateBack: () -> Unit
) {
    val activity = LocalActivity.current

    if (isLoading || errorMessage != null) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            if (!isLoading) {
                ErrorPlaceholder(
                    errorMessage = errorMessage!!.asString(),
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
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp),
                    strokeWidth = 4.5.dp
                )
            }
        }
    }
}