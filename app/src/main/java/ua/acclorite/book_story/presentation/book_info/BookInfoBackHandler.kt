package ua.acclorite.book_story.presentation.book_info

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
fun BookInfoBackHandler(
    navigateBack: () -> Unit
) {
    BackHandler {
        navigateBack()
    }
}