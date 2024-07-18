@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.screens.settings.nested.general.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsEvent
import ua.acclorite.book_story.presentation.screens.settings.nested.general.components.settings.AppLanguageSetting
import ua.acclorite.book_story.presentation.screens.settings.nested.general.components.settings.CheckForUpdatesSetting

/**
 * General Settings Category.
 * Contains all General settings such as App Language.
 *
 * @param state [MainState] instance.
 * @param onMainEvent [MainEvent] callback.
 * @param onSettingsEvent [SettingsEvent] callback.
 * @param topPadding Top padding to be applied.
 * @param bottomPadding Bottom padding to be applied.
 */
fun LazyListScope.GeneralSettingsCategory(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit,
    onSettingsEvent: (SettingsEvent) -> Unit,
    topPadding: Dp = 16.dp,
    bottomPadding: Dp = 48.dp
) {
    item {
        Spacer(
            modifier = Modifier
                .animateItem()
                .height((topPadding - 8.dp).coerceAtLeast(0.dp))
        )
    }

    item {
        AppLanguageSetting(
            state = state,
            onMainEvent = onMainEvent
        )
    }

    item {
        CheckForUpdatesSetting(
            state = state,
            onMainEvent = onMainEvent,
            onSettingsEvent = onSettingsEvent
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