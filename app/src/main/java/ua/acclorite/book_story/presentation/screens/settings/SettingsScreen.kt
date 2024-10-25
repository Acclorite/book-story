package ua.acclorite.book_story.presentation.screens.settings

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DisplaySettings
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.LocalLibrary
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.common.GoBackButton
import ua.acclorite.book_story.presentation.core.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.presentation.core.components.top_bar.collapsibleTopAppBarScrollBehavior
import ua.acclorite.book_story.presentation.core.navigation.LocalNavigator
import ua.acclorite.book_story.presentation.core.navigation.Screen
import ua.acclorite.book_story.presentation.screens.settings.components.SettingsCategoryItem

@Composable
fun SettingsScreenRoot() {
    SettingsScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen() {
    val onNavigate = LocalNavigator.current
    val (scrollBehavior, lazyListState) = TopAppBarDefaults.collapsibleTopAppBarScrollBehavior()

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(stringResource(id = R.string.settings_screen))
                },
                navigationIcon = {
                    GoBackButton(onNavigate = onNavigate)
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { paddingValues ->
        LazyColumnWithScrollbar(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            state = lazyListState,
            contentPadding = PaddingValues(top = 16.dp, bottom = 48.dp)
        ) {
            item {
                SettingsCategoryItem(
                    icon = Icons.Outlined.DisplaySettings,
                    title = stringResource(id = R.string.general_settings),
                    description = stringResource(id = R.string.general_settings_desc)
                ) {
                    onNavigate {
                        navigate(Screen.Settings.General)
                    }
                }
            }

            item {
                SettingsCategoryItem(
                    icon = Icons.Outlined.Palette,
                    title = stringResource(id = R.string.appearance_settings),
                    description = stringResource(id = R.string.appearance_settings_desc)
                ) {
                    onNavigate {
                        navigate(Screen.Settings.Appearance)
                    }
                }
            }

            item {
                SettingsCategoryItem(
                    icon = Icons.Outlined.LocalLibrary,
                    title = stringResource(id = R.string.reader_settings),
                    description = stringResource(id = R.string.reader_settings_desc)
                ) {
                    onNavigate {
                        navigate(Screen.Settings.ReaderSettings)
                    }
                }
            }

            item {
                SettingsCategoryItem(
                    icon = Icons.Outlined.Explore,
                    title = stringResource(id = R.string.browse_settings),
                    description = stringResource(id = R.string.browse_settings_desc)
                ) {
                    onNavigate {
                        navigate(Screen.Settings.BrowseSettings)
                    }
                }
            }
        }
    }
}