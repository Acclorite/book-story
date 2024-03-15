package ua.acclorite.book_story.presentation.screens.reader.components.settings_bottom_sheet

import androidx.activity.ComponentActivity
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ChipItem
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderViewModel
import ua.acclorite.book_story.presentation.screens.settings.components.CheckboxWithTitle
import ua.acclorite.book_story.presentation.screens.settings.components.ChipsWithTitle
import ua.acclorite.book_story.presentation.screens.settings.components.ColorPickerWithTitle
import ua.acclorite.book_story.presentation.screens.settings.components.SliderWithTitle
import ua.acclorite.book_story.ui.ElevationDefaults
import ua.acclorite.book_story.ui.elevation
import ua.acclorite.book_story.util.Constants

/**
 * Settings bottom sheet. Has General and Colors categories.
 */
@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class
)
@Composable
fun ReaderSettingsBottomSheet(mainViewModel: MainViewModel, viewModel: ReaderViewModel) {
    val pagerState = rememberPagerState { 2 }
    val currentPage by remember { derivedStateOf { pagerState.currentPage } }
    val context = LocalContext.current

    val navigationBarPadding =
        WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()

    val fontFamily = Constants.FONTS.find {
        it.id == mainViewModel.fontFamily.collectAsState().value!!
    } ?: Constants.FONTS[0]
    val fontStyle = mainViewModel.isItalic.collectAsState().value!!
    val fontSize = mainViewModel.fontSize.collectAsState().value!!
    val lineHeight = mainViewModel.lineHeight.collectAsState().value!!
    val paragraphHeight = mainViewModel.paragraphHeight.collectAsState().value!!
    val paragraphIndentation = mainViewModel.paragraphIndentation.collectAsState().value!!
    val backgroundColor = mainViewModel.backgroundColor.collectAsState().value!!
    val fontColor = mainViewModel.fontColor.collectAsState().value!!

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

    ModalBottomSheet(
        scrimColor = animatedScrimColor,
        dragHandle = {},
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(animatedHeight),
        onDismissRequest = {
            viewModel.onEvent(ReaderEvent.OnShowHideSettingsBottomSheet)
        },
        sheetState = rememberModalBottomSheetState(true),
        windowInsets = WindowInsets(0, 0, 0, 0),
        containerColor = MaterialTheme.elevation(ElevationDefaults.BottomSheet)
    ) {
        ReaderSettingsBottomSheetTabRow(viewModel = viewModel, pagerState = pagerState)

        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            LazyColumn(Modifier.fillMaxSize()) {
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
                                    ChipItem(
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
                                ChipItem(
                                    "normal",
                                    stringResource(id = R.string.font_style_normal),
                                    MaterialTheme.typography.labelLarge.copy(
                                        fontFamily = fontFamily.font,
                                        fontStyle = FontStyle.Normal
                                    ),
                                    !fontStyle
                                ),
                                ChipItem(
                                    "italic",
                                    stringResource(id = R.string.font_style_italic),
                                    MaterialTheme.typography.labelLarge.copy(
                                        fontFamily = fontFamily.font,
                                        fontStyle = FontStyle.Italic
                                    ),
                                    fontStyle
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
                            value = fontSize to "pt",
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
                            value = lineHeight to "pt",
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
                            value = paragraphHeight to "pt",
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
                            selected = paragraphIndentation,
                            title = stringResource(id = R.string.paragraph_indentation_option)
                        ) {
                            mainViewModel.onEvent(
                                MainEvent.OnChangeParagraphIndentation(!paragraphIndentation)
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
                            value = Color(backgroundColor.toULong()),
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
                            value = Color(fontColor.toULong()),
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











