/*
 * Codex — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 BlindMint
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.blindmint.codex.presentation.reader

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
import kotlinx.coroutines.launch
import ua.blindmint.codex.presentation.core.components.common.LazyColumnWithScrollbar
import ua.blindmint.codex.presentation.core.components.modal_bottom_sheet.ModalBottomSheet
import ua.blindmint.codex.presentation.core.util.LocalActivity
import ua.blindmint.codex.presentation.settings.appearance.colors.ColorsSubcategory
import ua.blindmint.codex.presentation.settings.reader.chapters.ChaptersSubcategory
import ua.blindmint.codex.presentation.settings.reader.font.FontSubcategory
import ua.blindmint.codex.presentation.settings.reader.images.ImagesSubcategory
import ua.blindmint.codex.presentation.settings.reader.misc.MiscSubcategory
import ua.blindmint.codex.presentation.settings.reader.padding.PaddingSubcategory
import ua.blindmint.codex.presentation.settings.reader.progress.ProgressSubcategory
import ua.blindmint.codex.presentation.settings.reader.reading_mode.ReadingModeSubcategory
import ua.blindmint.codex.presentation.settings.reader.reading_speed.ReadingSpeedSubcategory
import ua.blindmint.codex.presentation.settings.reader.system.SystemSubcategory
import ua.blindmint.codex.presentation.settings.reader.text.TextSubcategory
import ua.blindmint.codex.presentation.settings.reader.translator.TranslatorSubcategory
import ua.blindmint.codex.ui.reader.ReaderEvent

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
                        ReadingModeSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface }
                        )
                        PaddingSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface }
                        )
                        SystemSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface }
                        )
                        ReadingSpeedSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface }
                        )
                        MiscSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            showDivider = false
                        )
                    }
                }

                1 -> {
                    LazyColumnWithScrollbar(Modifier.fillMaxSize()) {
                        FontSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface }
                        )
                        TextSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface }
                        )
                        ImagesSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface }
                        )
                        ChaptersSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface }
                        )
                        ProgressSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface }
                        )
                        TranslatorSubcategory(
                            titleColor = { MaterialTheme.colorScheme.onSurface },
                            showDivider = false
                        )
                    }
                }

                2 -> {
                    LazyColumnWithScrollbar(Modifier.fillMaxSize()) {
                        ColorsSubcategory(
                            showTitle = false,
                            showDivider = false,
                            backgroundColor = { MaterialTheme.colorScheme.surfaceContainer }
                        )
                    }
                }
            }
        }
    }
}