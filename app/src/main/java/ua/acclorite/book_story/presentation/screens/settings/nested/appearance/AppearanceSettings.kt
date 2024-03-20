package ua.acclorite.book_story.presentation.screens.settings.nested.appearance

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ChipItem
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.screens.settings.components.ChipsWithTitle
import ua.acclorite.book_story.presentation.screens.settings.components.ColorPickerWithTitle
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.theme_switcher.AppearanceSettingsThemeSwitcher
import ua.acclorite.book_story.presentation.ui.DarkTheme
import ua.acclorite.book_story.presentation.ui.elevation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppearanceSettings(
    mainViewModel: MainViewModel,
    navigator: Navigator
) {
    val listState = rememberLazyListState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        canScroll = {
            listState.canScrollForward || listState.canScrollBackward
        }
    )

    val state by mainViewModel.state.collectAsState()

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
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
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                CategoryTitle(
                    title = stringResource(id = R.string.theme_appearance_settings),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                ChipsWithTitle(
                    title = stringResource(id = R.string.dark_theme_option),
                    chips = DarkTheme.entries.map {
                        ChipItem(
                            it.toString(),
                            when (it) {
                                DarkTheme.OFF -> stringResource(id = R.string.dark_theme_off)
                                DarkTheme.ON -> stringResource(id = R.string.dark_theme_on)
                                DarkTheme.FOLLOW_SYSTEM -> stringResource(id = R.string.dark_theme_follow_system)
                            },
                            MaterialTheme.typography.labelLarge,
                            it == state.darkTheme
                        )
                    },
                ) {
                    mainViewModel.onEvent(
                        MainEvent.OnChangeDarkTheme(
                            it.id
                        )
                    )
                }
            }

            item {
                AppearanceSettingsThemeSwitcher(mainViewModel = mainViewModel)
            }

            item {
                Spacer(modifier = Modifier.height(22.dp))
            }
            item {
                CategoryTitle(
                    title = stringResource(id = R.string.colors_appearance_settings),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            item {
                Spacer(modifier = Modifier.height(8.dp))
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

            item { Spacer(modifier = Modifier.height(48.dp)) }
        }
    }
}