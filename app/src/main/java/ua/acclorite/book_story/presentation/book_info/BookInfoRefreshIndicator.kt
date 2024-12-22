package ua.acclorite.book_story.presentation.book_info

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.PullRefreshState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun BoxScope.BookInfoRefreshIndicator(
    isUpdating: Boolean,
    checkingForUpdate: Boolean,
    refreshState: PullRefreshState,
    paddingValues: PaddingValues
) {
    PullRefreshIndicator(
        refreshing = isUpdating || checkingForUpdate,
        state = refreshState,
        Modifier
            .align(Alignment.TopCenter)
            .padding(top = paddingValues.calculateTopPadding()),
        backgroundColor = MaterialTheme.colorScheme.inverseSurface,
        contentColor = MaterialTheme.colorScheme.inverseOnSurface
    )
}