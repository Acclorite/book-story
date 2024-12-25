@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.browse.general

import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.settings.browse.general.components.BrowseGridSizeOption
import ua.acclorite.book_story.presentation.settings.browse.general.components.BrowseLayoutOption
import ua.acclorite.book_story.presentation.settings.browse.general.components.FilesStructureOption
import ua.acclorite.book_story.presentation.settings.browse.general.components.PinFavoriteDirectoriesOption
import ua.acclorite.book_story.presentation.settings.components.SettingsSubcategory

fun LazyListScope.BrowseGeneralSubcategory(
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    title: @Composable () -> String = { stringResource(id = R.string.general_browse_settings) },
    showTitle: Boolean = true,
    showDivider: Boolean = true
) {
    SettingsSubcategory(
        titleColor = titleColor,
        title = title,
        showTitle = showTitle,
        showDivider = showDivider
    ) {
        item {
            FilesStructureOption()
        }

        item {
            PinFavoriteDirectoriesOption()
        }

        item {
            BrowseLayoutOption()
        }

        item {
            BrowseGridSizeOption()
        }
    }
}