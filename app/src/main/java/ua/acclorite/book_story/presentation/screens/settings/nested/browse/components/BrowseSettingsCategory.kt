@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.screens.settings.nested.browse.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.State
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.subcategories.BrowseFilterSubcategory
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.subcategories.BrowseGeneralSubcategory
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.subcategories.BrowseSortSubcategory

/**
 * Browse Settings Category.
 * Contains all Browse settings.
 *
 * @param state [MainState] instance.
 * @param onMainEvent [MainEvent] callback.
 * @param topPadding Top padding to be applied.
 * @param bottomPadding Bottom padding to be applied.
 */
fun LazyListScope.BrowseSettingsCategory(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit,
    topPadding: Dp = 16.dp,
    bottomPadding: Dp = 48.dp
) {
    BrowseGeneralSubcategory(
        state = state,
        onMainEvent = onMainEvent,
        topPadding = topPadding,
        bottomPadding = 0.dp
    )
    BrowseFilterSubcategory(
        state = state,
        onMainEvent = onMainEvent,
        topPadding = 22.dp,
        bottomPadding = 0.dp
    )
    BrowseSortSubcategory(
        state = state,
        onMainEvent = onMainEvent,
        topPadding = 22.dp,
        bottomPadding = bottomPadding
    )
}