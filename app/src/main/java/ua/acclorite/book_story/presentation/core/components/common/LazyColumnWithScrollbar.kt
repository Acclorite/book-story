package ua.acclorite.book_story.presentation.core.components.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import my.nanihadesuka.compose.InternalLazyColumnScrollbar
import my.nanihadesuka.compose.ScrollbarSettings
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideSecondaryScrollbar

@Composable
fun LazyColumnWithScrollbar(
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    scrollbarSettings: ScrollbarSettings = Constants.provideSecondaryScrollbar(),
    enableScrollbar: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    verticalArrangement: Arrangement.Vertical = Arrangement.Top,
    content: LazyListScope.() -> Unit
) {
    val enabled = remember {
        derivedStateOf {
            enableScrollbar && (state.canScrollBackward || state.canScrollForward)
        }
    }

    Box {
        LazyColumn(
            modifier = modifier,
            state = state,
            verticalArrangement = verticalArrangement,
            contentPadding = contentPadding
        ) {
            content()
        }
        if (enabled.value) {
            InternalLazyColumnScrollbar(
                state = state,
                settings = scrollbarSettings
            )
        }
    }
}