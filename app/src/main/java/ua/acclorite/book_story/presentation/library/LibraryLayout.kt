package ua.acclorite.book_story.presentation.library

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.core.components.common.LazyVerticalGridWithScrollbar
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.providePrimaryScrollbar

@Composable
fun LibraryLayout(
    items: LazyGridScope.() -> Unit
) {
    LazyVerticalGridWithScrollbar(
        columns = GridCells.Adaptive(120.dp),
        modifier = Modifier.fillMaxSize(),
        scrollbarSettings = Constants.providePrimaryScrollbar(false),
        contentPadding = PaddingValues(8.dp)
    ) {
        items()
    }
}