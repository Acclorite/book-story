package ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.settings

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.components.SegmentedButtonWithTitle
import ua.acclorite.book_story.presentation.ui.BookStoryTheme
import ua.acclorite.book_story.presentation.ui.SlidingTransition
import ua.acclorite.book_story.presentation.ui.Theme
import ua.acclorite.book_story.presentation.ui.ThemeContrast
import ua.acclorite.book_story.presentation.ui.isDark
import ua.acclorite.book_story.presentation.ui.isPureDark

/**
 * Theme Contrast setting.
 * Lets user change theme contrast levels.
 */
@Composable
fun LazyItemScope.ThemeContrastSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    val themeContrastTheme = remember { mutableStateOf(state.value.theme!!) }

    LaunchedEffect(state.value.theme) {
        if (themeContrastTheme.value != state.value.theme && state.value.theme != Theme.DYNAMIC) {
            themeContrastTheme.value = state.value.theme!!
        }
    }

    BookStoryTheme(
        theme = themeContrastTheme.value,
        isDark = state.value.darkTheme!!.isDark(),
        isPureDark = state.value.pureDark!!.isPureDark(context = LocalContext.current),
        themeContrast = state.value.themeContrast!!
    ) {
        SlidingTransition(
            modifier = Modifier.animateItem(
                fadeInSpec = null,
                fadeOutSpec = null
            ),
            visible = state.value.theme != Theme.DYNAMIC,
        ) {
            SegmentedButtonWithTitle(
                title = stringResource(id = R.string.theme_contrast_option),
                enabled = state.value.theme != Theme.DYNAMIC,
                buttons = ThemeContrast.entries.map {
                    ButtonItem(
                        id = it.toString(),
                        title = when (it) {
                            ThemeContrast.STANDARD -> stringResource(id = R.string.theme_contrast_standard)
                            ThemeContrast.MEDIUM -> stringResource(id = R.string.theme_contrast_medium)
                            ThemeContrast.HIGH -> stringResource(id = R.string.theme_contrast_high)
                        },
                        textStyle = MaterialTheme.typography.labelLarge,
                        selected = it == state.value.themeContrast
                    )
                }
            ) {
                onMainEvent(
                    MainEvent.OnChangeThemeContrast(
                        it.id
                    )
                )
            }
        }
    }
}