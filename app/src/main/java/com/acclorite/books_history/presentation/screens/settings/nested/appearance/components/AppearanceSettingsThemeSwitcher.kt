package com.acclorite.books_history.presentation.screens.settings.nested.appearance.components

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.acclorite.books_history.R
import com.acclorite.books_history.presentation.components.CategoryTitle
import com.acclorite.books_history.presentation.data.MainEvent
import com.acclorite.books_history.presentation.data.MainViewModel
import com.acclorite.books_history.ui.Theme
import com.acclorite.books_history.ui.isDark
import com.acclorite.books_history.util.Constants

@Composable
fun AppearanceSettingsThemeSwitcher(
    mainViewModel: MainViewModel
) {
    val theme = mainViewModel.theme.collectAsState().value!!
    val darkTheme = mainViewModel.darkTheme.collectAsState().value!!

    val themes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Constants.THEMES
    else Constants.THEMES.dropWhile { it.first == Theme.DYNAMIC }

    Column(
        Modifier
            .fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        CategoryTitle(
            title = stringResource(id = R.string.app_theme_option)
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            Modifier
                .fillMaxWidth()
        ) {
            itemsIndexed(themes) { index, themeEntry ->
                if (index == 0)
                    Spacer(modifier = Modifier.width(18.dp))

                AppearanceSettingsThemeSwitcherItem(
                    theme = themeEntry,
                    darkTheme = darkTheme.isDark(),
                    selected = theme == themeEntry.first
                ) {
                    mainViewModel.onEvent(MainEvent.OnChangeTheme(themeEntry.first.toString()))
                }

                if (index != Theme.entries.lastIndex) {
                    Spacer(modifier = Modifier.width(8.dp))
                } else {
                    Spacer(modifier = Modifier.width(18.dp))
                }
            }
        }
    }
}