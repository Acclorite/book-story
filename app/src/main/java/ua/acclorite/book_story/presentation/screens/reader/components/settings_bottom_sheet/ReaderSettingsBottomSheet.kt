package ua.acclorite.book_story.presentation.screens.reader.components.settings_bottom_sheet

import androidx.activity.ComponentActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderViewModel
import ua.acclorite.book_story.presentation.screens.settings.components.CheckboxWithTitle
import ua.acclorite.book_story.presentation.screens.settings.components.ChipsWithTitle
import ua.acclorite.book_story.presentation.screens.settings.components.ColorPickerWithTitle
import ua.acclorite.book_story.presentation.screens.settings.components.SliderWithTitle

/**
 * Settings bottom sheet. Has General and Colors categories.
 */
@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun ReaderSettingsBottomSheet(mainViewModel: MainViewModel, viewModel: ReaderViewModel) {
    val pagerState = rememberPagerState { 2 }
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    val context = LocalContext.current

    val navigationBarPadding =
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val state by mainViewModel.state.collectAsState()

    val fontFamily = remember(state.fontFamily) {
        Constants.FONTS.find {
            it.id == state.fontFamily
        } ?: Constants.FONTS[0]
    }

    val scrimColor = if (currentPage == 1) Color.Transparent
    else BottomSheetDefaults.ScrimColor

    val animatedScrimColor by animateColorAsState(
        targetValue = scrimColor,
        animationSpec = tween(300),
        label = "Scrim animation"
    )

    val height = remember(currentPage) {
        if (currentPage == 1) 0.5f else 0.7f
    }
    val animatedHeight by animateFloatAsState(
        targetValue = height,
        animationSpec = tween(300),
        label = "Height animation"
    )

    LaunchedEffect(currentPage) {
        viewModel.onEvent(
            ReaderEvent.OnShowHideMenu(
                currentPage != 1,
                context as ComponentActivity
            )
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ModalBottomSheet(
            scrimColor = animatedScrimColor,
            dragHandle = {},
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .fillMaxHeight(animatedHeight),
            onDismissRequest = {
                viewModel.onEvent(ReaderEvent.OnShowHideSettingsBottomSheet)
            },
            sheetState = rememberModalBottomSheetState(true),
            windowInsets = WindowInsets(0, 0, 0, 0),
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            ReaderSettingsBottomSheetTabRow(viewModel = viewModel, pagerState = pagerState)

            HorizontalPager(state = pagerState) { page ->
                LazyColumn {
                    if (page == 0) {
                        item {
                            Spacer(modifier = Modifier.height(22.dp))
                        }
                        item {
                            CategoryTitle(
                                title = stringResource(id = R.string.font_reader_settings),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        item {
                            ChipsWithTitle(
                                title = stringResource(id = R.string.font_family_option),
                                chips = Constants.FONTS
                                    .map {
                                        ButtonItem(
                                            it.id,
                                            it.fontName.asString(),
                                            MaterialTheme.typography.labelLarge.copy(
                                                fontFamily = it.font
                                            ),
                                            it.id == fontFamily.id
                                        )
                                    },
                                onClick = {
                                    mainViewModel.onEvent(MainEvent.OnChangeFontFamily(it.id))
                                }
                            )
                        }
                        item {
                            ChipsWithTitle(
                                title = stringResource(id = R.string.font_style_option),
                                chips = listOf(
                                    ButtonItem(
                                        "normal",
                                        stringResource(id = R.string.font_style_normal),
                                        MaterialTheme.typography.labelLarge.copy(
                                            fontFamily = fontFamily.font,
                                            fontStyle = FontStyle.Normal
                                        ),
                                        !state.isItalic!!
                                    ),
                                    ButtonItem(
                                        "italic",
                                        stringResource(id = R.string.font_style_italic),
                                        MaterialTheme.typography.labelLarge.copy(
                                            fontFamily = fontFamily.font,
                                            fontStyle = FontStyle.Italic
                                        ),
                                        state.isItalic!!
                                    ),
                                ),
                                onClick = {
                                    mainViewModel.onEvent(
                                        MainEvent.OnChangeFontStyle(
                                            when (it.id) {
                                                "italic" -> true
                                                else -> false
                                            }
                                        )
                                    )
                                }
                            )
                        }
                        item {
                            SliderWithTitle(
                                value = state.fontSize!! to "pt",
                                fromValue = 10,
                                toValue = 35,
                                title = stringResource(id = R.string.font_size_option),
                                onValueChange = {
                                    mainViewModel.onEvent(
                                        MainEvent.OnChangeFontSize(it)
                                    )
                                }
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(22.dp))
                        }
                        item {
                            CategoryTitle(
                                title = stringResource(id = R.string.text_reader_settings),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                        item {
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                        item {
                            SliderWithTitle(
                                value = state.lineHeight!! to "pt",
                                fromValue = 1,
                                toValue = 16,
                                title = stringResource(id = R.string.line_height_option),
                                onValueChange = {
                                    mainViewModel.onEvent(
                                        MainEvent.OnChangeLineHeight(it)
                                    )
                                }
                            )
                        }
                        item {
                            SliderWithTitle(
                                value = state.paragraphHeight!! to "pt",
                                fromValue = 0,
                                toValue = 24,
                                title = stringResource(id = R.string.paragraph_height_option),
                                onValueChange = {
                                    mainViewModel.onEvent(
                                        MainEvent.OnChangeParagraphHeight(it)
                                    )
                                }
                            )
                        }
                        item {
                            CheckboxWithTitle(
                                selected = state.paragraphIndentation!!,
                                title = stringResource(id = R.string.paragraph_indentation_option)
                            ) {
                                mainViewModel.onEvent(
                                    MainEvent.OnChangeParagraphIndentation(!state.paragraphIndentation!!)
                                )
                            }
                        }
                    }

                    if (page == 1) {
                        item {
                            Spacer(modifier = Modifier.height(14.dp))
                        }
                        item {
                            ColorPickerWithTitle(
                                value = Color(state.backgroundColor!!.toULong()),
                                title = stringResource(id = R.string.background_color_option),
                                onValueChange = {
                                    mainViewModel.onEvent(
                                        MainEvent.OnChangeBackgroundColor(
                                            it.value.toLong()
                                        )
                                    )
                                }
                            )
                        }
                        item {
                            ColorPickerWithTitle(
                                value = Color(state.fontColor!!.toULong()),
                                title = stringResource(id = R.string.font_color_option),
                                onValueChange = {
                                    mainViewModel.onEvent(
                                        MainEvent.OnChangeFontColor(
                                            it.value.toLong()
                                        )
                                    )
                                }
                            )
                        }
                    }

                    item {
                        Spacer(
                            modifier = Modifier.height(
                                8.dp + navigationBarPadding
                            )
                        )
                    }
                }
            }
        }
    }

}











