@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.screens.settings.nested.browse.components

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.subcategories.BrowseFilterSubcategory
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.subcategories.BrowseGeneralSubcategory
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.subcategories.BrowseSortSubcategory

/**
 * Browse Settings Category.
 * Contains all Browse settings.
 *
 * @param topPadding Top padding to be applied.
 * @param bottomPadding Bottom padding to be applied.
 */
fun LazyListScope.BrowseSettingsCategory(
    topPadding: Dp = 16.dp,
    bottomPadding: Dp = 48.dp
) {
    BrowseGeneralSubcategory(
        topPadding = topPadding,
        bottomPadding = 0.dp
    )
    BrowseFilterSubcategory(
        topPadding = 22.dp,
        bottomPadding = 0.dp
    )
    BrowseSortSubcategory(
        showDivider = false,
        topPadding = 22.dp,
        bottomPadding = bottomPadding
    )
}