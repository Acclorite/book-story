@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.subcategories

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.settings.BrowseFilesStructureSetting
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.settings.BrowseGridSizeSetting
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.settings.BrowseLayoutSetting
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.settings.BrowsePinFavoriteDirectoriesSetting

/**
 * Browse General subcategory.
 * Contains all settings from General Browse settings.
 */
fun LazyListScope.BrowseGeneralSubcategory(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit,
    titleColor: @Composable () -> Color = { MaterialTheme.colorScheme.primary },
    title: @Composable () -> String = { stringResource(id = R.string.general_browse_settings) },
    showTitle: Boolean = true,
    topPadding: Dp,
    bottomPadding: Dp
) {
    item {
        if (showTitle) {
            CategoryTitle(
                title = title.invoke(),
                color = titleColor.invoke(),
                modifier = Modifier
                    .animateItem()
                    .padding(
                        top = topPadding,
                        bottom = 8.dp
                    )
            )
        } else {
            Spacer(
                modifier = Modifier
                    .animateItem()
                    .height((topPadding - 8.dp).coerceAtLeast(0.dp))
            )
        }
    }

    item {
        BrowseFilesStructureSetting(
            state = state,
            onMainEvent = onMainEvent
        )
    }

    item {
        BrowsePinFavoriteDirectoriesSetting(
            state = state,
            onMainEvent = onMainEvent
        )
    }

    item {
        BrowseLayoutSetting(
            state = state,
            onMainEvent = onMainEvent
        )
    }

    item {
        BrowseGridSizeSetting(
            state = state,
            onMainEvent = onMainEvent
        )
    }

    item {
        Spacer(
            modifier = Modifier
                .animateItem()
                .height(bottomPadding)
        )
    }
}