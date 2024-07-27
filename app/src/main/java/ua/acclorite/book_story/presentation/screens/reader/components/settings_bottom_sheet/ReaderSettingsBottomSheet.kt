package ua.acclorite.book_story.presentation.screens.reader.components.settings_bottom_sheet

import androidx.activity.ComponentActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.components.CustomBottomSheet
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsEvent
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsState
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.subcategories.ColorsSubcategory
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.ReaderSettingsCategory

/**
 * Settings bottom sheet. Has General and Colors categories.
 *
 * @param mainState [MainState].
 * @param settingsState [SettingsState].
 * @param onEvent [ReaderEvent] callback.
 * @param onMainEvent [MainEvent] callback.
 * @param onSettingsEvent [SettingsEvent] callback.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSettingsBottomSheet(
    mainState: State<MainState>,
    settingsState: State<SettingsState>,
    onEvent: (ReaderEvent) -> Unit,
    onMainEvent: (MainEvent) -> Unit,
    onSettingsEvent: (SettingsEvent) -> Unit
) {
    val pagerState = rememberPagerState { 2 }
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    val context = LocalContext.current

    val scrimColor = if (currentPage == 1) Color.Transparent
    else BottomSheetDefaults.ScrimColor

    val animatedScrimColor by animateColorAsState(
        targetValue = scrimColor,
        animationSpec = tween(300),
        label = "Scrim animation"
    )

    val height = remember(currentPage) {
        if (currentPage == 1) 0.6f else 0.7f
    }
    val animatedHeight by animateFloatAsState(
        targetValue = height,
        animationSpec = tween(300),
        label = "Height animation"
    )

    LaunchedEffect(currentPage) {
        onEvent(
            ReaderEvent.OnShowHideMenu(
                currentPage != 1,
                context as ComponentActivity
            )
        )
    }

    CustomBottomSheet(
        hasFixedHeight = true,
        scrimColor = animatedScrimColor,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(animatedHeight),
        dragHandle = {},
        onDismissRequest = {
            onEvent(ReaderEvent.OnShowHideSettingsBottomSheet)
        }
    ) {
        ReaderSettingsBottomSheetTabRow(
            onEvent = onEvent,
            pagerState = pagerState
        )

        HorizontalPager(state = pagerState) { page ->
            LazyColumn {
                if (page == 0) {
                    ReaderSettingsCategory(
                        state = mainState,
                        onMainEvent = onMainEvent,
                        titleColor = { MaterialTheme.colorScheme.onSurface },
                        topPadding = 16.dp,
                        bottomPadding = 8.dp + it
                    )
                }
            }

            LazyColumn {
                if (page == 1) {
                    ColorsSubcategory(
                        state = mainState,
                        settingsState = settingsState,
                        onMainEvent = onMainEvent,
                        onSettingsEvent = onSettingsEvent,
                        showTitle = false,
                        backgroundColor = { MaterialTheme.colorScheme.surfaceContainer },
                        topPadding = 16.dp,
                        bottomPadding = 8.dp + it
                    )
                }
            }
        }
    }
}