package ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.theme_switcher

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.ui.Theme
import ua.acclorite.book_story.presentation.ui.isDark

/**
 * Theme switcher.
 */
@Composable
fun AppearanceSettingsThemeSwitcher(
    mainViewModel: MainViewModel
) {
    val state by mainViewModel.state.collectAsState()
    val themes = remember {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) Constants.THEMES
        else Constants.THEMES.dropWhile { it.first == Theme.DYNAMIC }
    }

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
                .fillMaxWidth(),
        ) {
            itemsIndexed(themes) { index, themeEntry ->
                if (index == 0) {
                    Spacer(modifier = Modifier.width(18.dp))
                } else {
                    Spacer(modifier = Modifier.width(8.dp))
                }

                AppearanceSettingsThemeSwitcherItem(
                    theme = themeEntry,
                    darkTheme = state.darkTheme!!.isDark(),
                    themeContrast = state.themeContrast!!,
                    selected = state.theme == themeEntry.first
                ) {
                    mainViewModel.onEvent(MainEvent.OnChangeTheme(themeEntry.first.toString()))
                }

                if (index == themes.lastIndex) {
                    Spacer(modifier = Modifier.width(18.dp))
                }
            }
        }
    }
}