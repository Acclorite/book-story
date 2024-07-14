package ua.acclorite.book_story.presentation.screens.settings.nested.reader

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.components.collapsibleUntilExitScrollBehaviorWithLazyListState
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.screens.settings.components.ChipsWithTitle
import ua.acclorite.book_story.presentation.screens.settings.components.SliderWithTitle
import ua.acclorite.book_story.presentation.screens.settings.components.SwitchWithTitle

@Composable
fun ReaderSettingsRoot() {
    val navigator = LocalNavigator.current
    val mainViewModel: MainViewModel = hiltViewModel()

    val state = mainViewModel.state.collectAsState()

    ReaderSettings(
        state = state,
        onNavigate = { navigator.it() },
        onMainEvent = mainViewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ReaderSettings(
    state: State<MainState>,
    onNavigate: OnNavigate,
    onMainEvent: (MainEvent) -> Unit
) {
    val scrollState = TopAppBarDefaults.collapsibleUntilExitScrollBehaviorWithLazyListState()

    val fontFamily = remember(state.value.fontFamily) {
        Constants.FONTS.find {
            it.id == state.value.fontFamily
        } ?: Constants.FONTS[0]
    }

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollState.first.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(stringResource(id = R.string.reader_settings))
                },
                navigationIcon = {
                    GoBackButton(onNavigate = onNavigate)
                },
                scrollBehavior = scrollState.first,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            state = scrollState.second
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
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
                        onMainEvent(MainEvent.OnChangeFontFamily(it.id))
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
                            !state.value.isItalic!!
                        ),
                        ButtonItem(
                            "italic",
                            stringResource(id = R.string.font_style_italic),
                            MaterialTheme.typography.labelLarge.copy(
                                fontFamily = fontFamily.font,
                                fontStyle = FontStyle.Italic
                            ),
                            state.value.isItalic!!
                        ),
                    ),
                    onClick = {
                        onMainEvent(
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
                    value = state.value.fontSize!! to "pt",
                    fromValue = 10,
                    toValue = 35,
                    title = stringResource(id = R.string.font_size_option),
                    onValueChange = {
                        onMainEvent(
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
                    value = state.value.sidePadding!! to "pt",
                    fromValue = 1,
                    toValue = 20,
                    title = stringResource(id = R.string.side_padding_option),
                    onValueChange = {
                        onMainEvent(
                            MainEvent.OnChangeSidePadding(it)
                        )
                    }
                )
            }
            item {
                SliderWithTitle(
                    value = state.value.lineHeight!! to "pt",
                    fromValue = 1,
                    toValue = 16,
                    title = stringResource(id = R.string.line_height_option),
                    onValueChange = {
                        onMainEvent(
                            MainEvent.OnChangeLineHeight(it)
                        )
                    }
                )
            }
            item {
                SliderWithTitle(
                    value = state.value.paragraphHeight!! to "pt",
                    fromValue = 0,
                    toValue = 24,
                    title = stringResource(id = R.string.paragraph_height_option),
                    onValueChange = {
                        onMainEvent(
                            MainEvent.OnChangeParagraphHeight(it)
                        )
                    }
                )
            }
            item {
                SwitchWithTitle(
                    selected = state.value.paragraphIndentation!!,
                    title = stringResource(id = R.string.paragraph_indentation_option)
                ) {
                    onMainEvent(
                        MainEvent.OnChangeParagraphIndentation(!state.value.paragraphIndentation!!)
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }
        }
    }
}