package com.acclorite.books_history.presentation.screens.settings.nested.general.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acclorite.books_history.R
import com.acclorite.books_history.presentation.components.CustomDialogWithLazyColumn
import com.acclorite.books_history.presentation.components.SelectableDialogItem
import com.acclorite.books_history.presentation.main.MainEvent
import com.acclorite.books_history.presentation.main.MainViewModel
import com.acclorite.books_history.presentation.screens.settings.nested.general.data.GeneralSettingsEvent
import com.acclorite.books_history.presentation.screens.settings.nested.general.data.GeneralSettingsViewModel
import com.acclorite.books_history.util.Constants

@Composable
fun GeneralSettingsLanguageDialog(
    viewModel: GeneralSettingsViewModel,
    mainViewModel: MainViewModel
) {
    val language = mainViewModel.language.collectAsState().value!!
    val activity = LocalContext.current as ComponentActivity

    CustomDialogWithLazyColumn(
        title = stringResource(id = R.string.language_option),
        imageVectorIcon = null,
        description = null,
        actionText = null,
        isActionEnabled = null,
        onDismiss = { viewModel.onEvent(GeneralSettingsEvent.OnShowHideLanguageDialog) },
        onAction = {},
        withDivider = false,
        items = {
            item {
                Spacer(modifier = Modifier.height(12.dp))
            }
            items(Constants.LANGUAGES) { lang ->
                SelectableDialogItem(selected = lang.first == language, title = lang.second) {
                    mainViewModel.onEvent(
                        MainEvent.OnChangeLanguage(
                            lang.first,
                            activity
                        )
                    )
                }
            }
        }
    )
}