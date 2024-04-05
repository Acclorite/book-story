package ua.acclorite.book_story.presentation.screens.settings.nested.appearance

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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.components.collapsibleScrollBehaviorWithLazyListState
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.screens.settings.components.ColorPickerWithTitle
import ua.acclorite.book_story.presentation.screens.settings.components.SegmentedButtonWithTitle
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.theme_switcher.AppearanceSettingsThemeSwitcher
import ua.acclorite.book_story.presentation.ui.BookStoryTheme
import ua.acclorite.book_story.presentation.ui.DarkTheme
import ua.acclorite.book_story.presentation.ui.PureDark
import ua.acclorite.book_story.presentation.ui.SlidingTransition
import ua.acclorite.book_story.presentation.ui.Theme
import ua.acclorite.book_story.presentation.ui.ThemeContrast
import ua.acclorite.book_story.presentation.ui.isDark
import ua.acclorite.book_story.presentation.ui.isPureDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSettings(
    mainViewModel: MainViewModel,
    navigator: Navigator
) {
    val scrollState = TopAppBarDefaults.collapsibleScrollBehaviorWithLazyListState()

    val state by mainViewModel.state.collectAsState()
    var themeContrastTheme by remember { mutableStateOf(state.theme!!) }

    LaunchedEffect(state.theme) {
        if (themeContrastTheme != state.theme && state.theme != Theme.DYNAMIC) {
            themeContrastTheme = state.theme!!
        }
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
                    Text(stringResource(id = R.string.appearance_settings))
                },
                navigationIcon = {
                    GoBackButton(navigator = navigator)
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
                Spacer(
                    modifier = Modifier
                        .animateItem()
                        .height(16.dp)
                )
            }
            item {
                CategoryTitle(
                    modifier = Modifier.animateItem(),
                    title = stringResource(id = R.string.theme_appearance_settings),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                Spacer(
                    modifier = Modifier
                        .animateItem()
                        .height(8.dp)
                )
            }

            item {
                SegmentedButtonWithTitle(
                    modifier = Modifier.animateItem(),
                    title = stringResource(id = R.string.dark_theme_option),
                    locked = true,
                    buttons = DarkTheme.entries.map {
                        ButtonItem(
                            it.toString(),
                            when (it) {
                                DarkTheme.OFF -> stringResource(id = R.string.dark_theme_off)
                                DarkTheme.ON -> stringResource(id = R.string.dark_theme_on)
                                DarkTheme.FOLLOW_SYSTEM -> stringResource(id = R.string.dark_theme_follow_system)
                            },
                            MaterialTheme.typography.labelLarge,
                            it == state.darkTheme
                        )
                    }
                ) {
                    mainViewModel.onEvent(
                        MainEvent.OnChangeDarkTheme(
                            it.id
                        )
                    )
                }
            }

            item {
                AppearanceSettingsThemeSwitcher(
                    modifier = Modifier.animateItem(),
                    mainViewModel = mainViewModel
                )
            }

            item {
                BookStoryTheme(
                    theme = themeContrastTheme,
                    isDark = state.darkTheme!!.isDark(),
                    isPureDark = state.pureDark!!.isPureDark(context = LocalContext.current),
                    themeContrast = state.themeContrast!!
                ) {
                    SlidingTransition(
                        modifier = Modifier.animateItem(
                            fadeInSpec = null,
                            fadeOutSpec = null
                        ),
                        visible = state.theme != Theme.DYNAMIC,
                    ) {
                        SegmentedButtonWithTitle(
                            title = stringResource(id = R.string.theme_contrast_option),
                            locked = state.theme != Theme.DYNAMIC,
                            buttons = ThemeContrast.entries.map {
                                ButtonItem(
                                    it.toString(),
                                    when (it) {
                                        ThemeContrast.STANDARD -> stringResource(id = R.string.theme_contrast_standard)
                                        ThemeContrast.MEDIUM -> stringResource(id = R.string.theme_contrast_medium)
                                        ThemeContrast.HIGH -> stringResource(id = R.string.theme_contrast_high)
                                    },
                                    MaterialTheme.typography.labelLarge,
                                    it == state.themeContrast
                                )
                            }
                        ) {
                            mainViewModel.onEvent(
                                MainEvent.OnChangeThemeContrast(
                                    it.id
                                )
                            )
                        }
                    }
                }
            }

            item {
                SlidingTransition(
                    modifier = Modifier.animateItem(
                        fadeInSpec = null,
                        fadeOutSpec = null
                    ),
                    visible = state.darkTheme!!.isDark(),
                ) {
                    SegmentedButtonWithTitle(
                        title = stringResource(id = R.string.pure_dark_option),
                        locked = true,
                        buttons = PureDark.entries.map {
                            ButtonItem(
                                it.toString(),
                                when (it) {
                                    PureDark.OFF -> stringResource(id = R.string.pure_dark_off)
                                    PureDark.ON -> stringResource(id = R.string.pure_dark_on)
                                    PureDark.SAVER -> stringResource(id = R.string.pure_dark_power_saver)
                                },
                                MaterialTheme.typography.labelLarge,
                                it == state.pureDark
                            )
                        }
                    ) {
                        mainViewModel.onEvent(
                            MainEvent.OnChangePureDark(
                                it.id
                            )
                        )
                    }
                }
            }

            item {
                Spacer(
                    modifier = Modifier
                        .animateItem()
                        .height(22.dp)
                )
                CategoryTitle(
                    title = stringResource(id = R.string.colors_appearance_settings),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.animateItem()
                )
                Spacer(
                    modifier = Modifier
                        .animateItem()
                        .height(8.dp)
                )
            }

            item {
                ColorPickerWithTitle(
                    modifier = Modifier.animateItem(),
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
                    modifier = Modifier.animateItem(),
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

            item {
                Spacer(
                    modifier = Modifier
                        .animateItem()
                        .height(48.dp)
                )
            }
        }
    }
}