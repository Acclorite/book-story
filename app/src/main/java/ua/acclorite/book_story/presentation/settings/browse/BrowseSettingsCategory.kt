@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.browse

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.settings.browse.filter.BrowseFilterSubcategory
import ua.acclorite.book_story.presentation.settings.browse.general.BrowseGeneralSubcategory
import ua.acclorite.book_story.presentation.settings.browse.sort.BrowseSortSubcategory

fun LazyListScope.BrowseSettingsCategory(
    topPadding: Dp = 16.dp,
    bottomPadding: Dp = 16.dp
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