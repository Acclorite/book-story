package com.acclorite.books_history.presentation.screens.settings.nested.appearance.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acclorite.books_history.R
import com.acclorite.books_history.presentation.components.CustomDialogWithLazyColumn
import com.acclorite.books_history.presentation.components.SelectableDialogItem
import com.acclorite.books_history.presentation.main.MainEvent
import com.acclorite.books_history.presentation.main.MainViewModel
import com.acclorite.books_history.presentation.screens.settings.nested.appearance.data.AppearanceSettingsEvent
import com.acclorite.books_history.presentation.screens.settings.nested.appearance.data.AppearanceSettingsViewModel
import com.acclorite.books_history.ui.DarkTheme

@Composable
fun AppearanceSettingsDarkThemeDialog(
    viewModel: AppearanceSettingsViewModel,
    mainViewModel: MainViewModel
) {
    val darkTheme = mainViewModel.darkTheme.collectAsState().value!!

    CustomDialogWithLazyColumn(
        title = stringResource(id = R.string.dark_theme_option),
        imageVectorIcon = null,
        description = null,
        actionText = null,
        isActionEnabled = null,
        onDismiss = { viewModel.onEvent(AppearanceSettingsEvent.OnShowHideDarkThemeDialog) },
        onAction = {},
        withDivider = false,
        items = {
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            items(DarkTheme.entries) {
                val theme = when (it) {
                    DarkTheme.OFF -> stringResource(id = R.string.dark_theme_off)
                    DarkTheme.ON -> stringResource(id = R.string.dark_theme_on)
                    DarkTheme.FOLLOW_SYSTEM -> stringResource(id = R.string.dark_theme_follow_system)
                }

                SelectableDialogItem(selected = it == darkTheme, title = theme) {
                    mainViewModel.onEvent(
                        MainEvent.OnChangeDarkTheme(
                            it.toString()
                        )
                    )
                }
            }
        }
    )
}