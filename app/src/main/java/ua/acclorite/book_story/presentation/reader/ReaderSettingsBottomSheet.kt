package ua.acclorite.book_story.presentation.reader

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ua.acclorite.book_story.presentation.core.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.presentation.core.components.modal_bottom_sheet.ModalBottomSheet
import ua.acclorite.book_story.presentation.core.util.LocalActivity
import ua.acclorite.book_story.presentation.settings.appearance.colors.ColorsSubcategory
import ua.acclorite.book_story.presentation.settings.reader.font.FontSubcategory
import ua.acclorite.book_story.presentation.settings.reader.misc.MiscSubcategory
import ua.acclorite.book_story.presentation.settings.reader.padding.PaddingSubcategory
import ua.acclorite.book_story.presentation.settings.reader.reading_speed.ReadingSpeedSubcategory
import ua.acclorite.book_story.presentation.settings.reader.system.SystemSubcategory
import ua.acclorite.book_story.presentation.settings.reader.text.TextSubcategory
import ua.acclorite.book_story.presentation.settings.reader.translator.TranslatorSubcategory
import ua.acclorite.book_story.ui.reader.ReaderEvent

private var initialPage = 0

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSettingsBottomSheet(
    fullscreenMode: Boolean,
    menuVisibility: (ReaderEvent.OnMenuVisibility) -> Unit,
    dismissBottomSheet: (ReaderEvent.OnDismissBottomSheet) -> Unit
) {
    val activity = LocalActivity.current
    val scope = rememberCoroutineScope()
    val pagerState = rememberPagerState(initialPage) { 3 }
    DisposableEffect(Unit) { onDispose { initialPage = pagerState.currentPage } }

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
        menuVisibility(
            ReaderEvent.OnMenuVisibility(
                show = pagerState.currentPage != 2,
                fullscreenMode = fullscreenMode,
                saveCheckpoint = false,
                activity = activity
            )
        )
    }

    ModalBottomSheet(
        hasFixedHeight = true,
        scrimColor = animatedScrimColor,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(animatedHeight),
        dragHandle = {},
        onDismissRequest = {
            dismissBottomSheet(ReaderEvent.OnDismissBottomSheet)
        },
        sheetGesturesEnabled = false
    ) {
        ReaderSettingsBottomSheetTabRow(
            currentPage = pagerState.currentPage,
            scrollToPage = {
                scope.launch {
                    pagerState.animateScrollToPage(it)
                }
            }
        )

        HorizontalPager(state = pagerState) { page ->
            when (page) {
                0 -> {
                    LazyColumnWithScrollbar(Modifier.fillMaxSize()) {
                        PaddingSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            topPadding = 22.dp,
                            bottomPadding = 0.dp
                        )
                        SystemSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            topPadding = 22.dp,
                            bottomPadding = 0.dp
                        )
                        ReadingSpeedSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            topPadding = 22.dp,
                            bottomPadding = 0.dp
                        )
                        MiscSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            showDivider = false,
                            topPadding = 22.dp,
                            bottomPadding = 8.dp
                        )
                    }
                }

                1 -> {
                    LazyColumnWithScrollbar(Modifier.fillMaxSize()) {
                        FontSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            topPadding = 16.dp,
                            bottomPadding = 0.dp
                        )
                        TextSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            topPadding = 22.dp,
                            bottomPadding = 0.dp
                        )
                        TranslatorSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            showDivider = false,
                            topPadding = 22.dp,
                            bottomPadding = 8.dp
                        )
                    }
                }

                2 -> {
                    LazyColumnWithScrollbar(Modifier.fillMaxSize()) {
                        ColorsSubcategory(
                            showTitle = false,
                            showDivider = false,
                            backgroundColor = { MaterialTheme.colorScheme.surfaceContainer },
                            topPadding = 16.dp,
                            bottomPadding = 8.dp
                        )
                    }
                }
            }
        }
    }
}