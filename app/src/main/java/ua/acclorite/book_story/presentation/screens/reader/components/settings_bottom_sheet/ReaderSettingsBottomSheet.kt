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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.presentation.components.CustomBottomSheet
import ua.acclorite.book_story.presentation.components.LocalReaderViewModel
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.subcategories.ColorsSubcategory
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.components.ReaderSettingsCategory

/**
 * Settings bottom sheet. Has General and Colors categories.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSettingsBottomSheet() {
    val state = LocalReaderViewModel.current.state
    val onEvent = LocalReaderViewModel.current.onEvent
    val context = LocalContext.current

    val pagerState = rememberPagerState(state.value.currentPage) { 3 }

    val animatedScrimColor by animateColorAsState(
        targetValue = if (pagerState.currentPage == 2) Color.Transparent
        else BottomSheetDefaults.ScrimColor,
        animationSpec = tween(300)
    )
    val animatedHeight by animateFloatAsState(
        targetValue = if (pagerState.currentPage == 2) 0.6f else 0.7f,
        animationSpec = tween(300)
    )

    LaunchedEffect(pagerState.currentPage) {
        onEvent(
            ReaderEvent.OnScrollToSettingsPage(
                page = pagerState.currentPage,
                pagerState = null
            )
        )
        onEvent(
            ReaderEvent.OnShowHideMenu(
                pagerState.currentPage != 2,
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
        ReaderSettingsBottomSheetTabRow(pagerState = pagerState)

        HorizontalPager(state = pagerState) { page ->
            LazyColumn {
                if (page == 0) {
            if (page == 1) {
                LazyColumn {
                    ReaderSettingsCategory(
                        titleColor = { MaterialTheme.colorScheme.onSurface },
                        topPadding = 16.dp,
                        bottomPadding = 8.dp + it
                    )
                }
            }

            if (page == 2) {
                LazyColumn {
                    ColorsSubcategory(
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