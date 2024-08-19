package ua.acclorite.book_story.presentation.screens.settings

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.DisplaySettings
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Palette
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
import ua.acclorite.book_story.domain.util.OnNavigate
import ua.acclorite.book_story.presentation.components.GoBackButton
import ua.acclorite.book_story.presentation.components.collapsibleUntilExitScrollBehaviorWithLazyListState
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.settings.components.SettingsCategoryItem

@Composable
fun SettingsScreenRoot() {
    val navigator = LocalNavigator.current

    SettingsScreen(onNavigate = { navigator.it() })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    onNavigate: OnNavigate
) {
    val scrollState = TopAppBarDefaults.collapsibleUntilExitScrollBehaviorWithLazyListState()

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollState.first.nestedScrollConnection)
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
                SettingsCategoryItem(
                    icon = Icons.Default.DisplaySettings,
                    text = stringResource(id = R.string.general_settings),
                    description = stringResource(id = R.string.general_settings_desc)
                ) {
                    onNavigate {
                        navigate(Screen.Settings.General)
                    }
                }
            }

            item {
                SettingsCategoryItem(
                    icon = Icons.Default.Palette,
                    text = stringResource(id = R.string.appearance_settings),
                    description = stringResource(id = R.string.appearance_settings_desc)
                ) {
                    onNavigate {
                        navigate(Screen.Settings.Appearance)
                    }
                }
            }

            item {
                SettingsCategoryItem(
                    icon = Icons.AutoMirrored.Filled.MenuBook,
                    text = stringResource(id = R.string.reader_settings),
                    description = stringResource(id = R.string.reader_settings_desc)
                ) {
                    onNavigate {
                        navigate(Screen.Settings.ReaderSettings)
                    }
                }
            }

            item {
                SettingsCategoryItem(
                    icon = Icons.Default.Explore,
                    text = stringResource(id = R.string.browse_settings),
                    description = stringResource(id = R.string.browse_settings_desc)
                ) {
                    onNavigate {
                        navigate(Screen.Settings.BrowseSettings)
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(48.dp)) }
        }
    }
}