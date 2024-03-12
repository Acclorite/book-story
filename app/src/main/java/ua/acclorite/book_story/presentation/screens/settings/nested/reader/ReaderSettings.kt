package ua.acclorite.book_story.presentation.screens.settings.nested.reader

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ChipItem
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.screens.settings.components.CheckboxWithTitle
import ua.acclorite.book_story.presentation.screens.settings.components.ChipsWithTitle
import ua.acclorite.book_story.presentation.screens.settings.components.SliderWithTitle
import ua.acclorite.book_story.ui.elevation
import ua.acclorite.book_story.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderSettings(
    mainViewModel: MainViewModel,
    navigator: Navigator
) {
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        canScroll = {
            listState.canScrollForward
        }
    )

    val fontFamily = Constants.FONTS.find {
        it.id == mainViewModel.fontFamily.collectAsState().value!!
    } ?: Constants.FONTS[0]
    val fontStyle = mainViewModel.isItalic.collectAsState().value!!
    val fontSize = mainViewModel.fontSize.collectAsState().value!!
    val lineHeight = mainViewModel.lineHeight.collectAsState().value!!
    val paragraphHeight = mainViewModel.paragraphHeight.collectAsState().value!!
    val paragraphIndentation = mainViewModel.paragraphIndentation.collectAsState().value!!

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(stringResource(id = R.string.reader_settings))
                },
                navigationIcon = {
                    GoBackButton(navigator = navigator)
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.elevation()
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            state = listState
        ) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }
            item {
                CategoryTitle(
                    title = stringResource(id = R.string.font_reader_settings),
                    color = MaterialTheme.colorScheme.primary
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
                    color = MaterialTheme.colorScheme.primary
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
    }
}