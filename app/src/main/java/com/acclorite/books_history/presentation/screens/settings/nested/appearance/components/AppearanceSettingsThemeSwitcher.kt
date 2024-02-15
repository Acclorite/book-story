package com.acclorite.books_history.presentation.screens.settings.nested.appearance.components

import android.os.Build
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acclorite.books_history.R
import com.acclorite.books_history.presentation.main.MainEvent
import com.acclorite.books_history.presentation.main.MainViewModel
import com.acclorite.books_history.ui.Theme
import com.acclorite.books_history.ui.isDark

@Composable
fun AppearanceSettingsThemeSwitcher(
    mainViewModel: MainViewModel
) {
    val theme = mainViewModel.theme.collectAsState().value!!
    val darkTheme = mainViewModel.darkTheme.collectAsState().value!!

    val themes = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Theme.entries
    else Theme.entries.dropWhile { it == Theme.DYNAMIC }

    Column(
        Modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.app_theme_option),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 18.sp,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            Modifier
                .fillMaxWidth()
        ) {
            itemsIndexed(themes) { index, themeEntry ->
                if (index == 0)
                    Spacer(modifier = Modifier.width(16.dp))

                AppearanceSettingsThemeSwitcherItem(
                    theme = themeEntry,
                    darkTheme = darkTheme.isDark(),
                    selected = theme == themeEntry
                ) {
                    mainViewModel.onEvent(MainEvent.OnChangeTheme(themeEntry.toString()))
                }

                if (index != Theme.entries.lastIndex) {
                    Spacer(modifier = Modifier.width(8.dp))
                } else {
                    Spacer(modifier = Modifier.width(16.dp))
                }
            }
        }
    }
}