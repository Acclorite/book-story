package ua.acclorite.book_story.presentation.core.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import my.nanihadesuka.compose.InternalLazyVerticalGridScrollbar
import my.nanihadesuka.compose.ScrollbarSettings
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideSecondaryScrollbar

@Composable
fun LazyVerticalGridWithScrollbar(
    modifier: Modifier = Modifier,
    columns: GridCells,
    state: LazyGridState = rememberLazyGridState(),
    scrollbarSettings: ScrollbarSettings = Constants.provideSecondaryScrollbar(),
    enableScrollbar: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: LazyGridScope.() -> Unit
) {
    val enabled = remember {
        derivedStateOf {
            enableScrollbar && (state.canScrollBackward || state.canScrollForward)
        }
    }

    Box {
        LazyVerticalGrid(
            modifier = modifier,
            columns = columns,
            state = state,
            verticalArrangement = verticalArrangement,
            contentPadding = contentPadding
        ) {
            content()
        }
        if (enabled.value) {
            InternalLazyVerticalGridScrollbar(
                state = state,
                settings = scrollbarSettings
            )
        }
    }
}