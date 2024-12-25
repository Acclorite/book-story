@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.browse

import androidx.compose.foundation.lazy.LazyListScope
import ua.acclorite.book_story.presentation.settings.browse.filter.BrowseFilterSubcategory
import ua.acclorite.book_story.presentation.settings.browse.general.BrowseGeneralSubcategory
import ua.acclorite.book_story.presentation.settings.browse.sort.BrowseSortSubcategory

fun LazyListScope.BrowseSettingsCategory() {
    BrowseGeneralSubcategory()
    BrowseFilterSubcategory()
    BrowseSortSubcategory(
        showDivider = false
    )
}