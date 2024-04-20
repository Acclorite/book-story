package ua.acclorite.book_story.presentation.screens.start.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ButtonItem
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainSettingsState
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

/**
 * Appearance settings.
 */
fun LazyListScope.startAppearanceScreen(
    mainState: State<MainSettingsState>,
    onMainEvent: (MainEvent) -> Unit,
    themeContrastTheme: State<Theme>
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
            title = stringResource(id = R.string.start_theme_preferences),
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
                    it == mainState.value.darkTheme
                )
            }
        ) {
            onMainEvent(
                MainEvent.OnChangeDarkTheme(
                    it.id
                )
            )
        }
    }

    item {
        AppearanceSettingsThemeSwitcher(
            modifier = Modifier.animateItem(),
            state = mainState,
            onMainEvent = onMainEvent
        )
    }

    item {
        BookStoryTheme(
            theme = themeContrastTheme.value,
            isDark = mainState.value.darkTheme!!.isDark(),
            isPureDark = mainState.value.pureDark!!.isPureDark(context = LocalContext.current),
            themeContrast = mainState.value.themeContrast!!
        ) {
            SlidingTransition(
                modifier = Modifier.animateItem(
                    fadeInSpec = null,
                    fadeOutSpec = null
                ),
                visible = mainState.value.theme != Theme.DYNAMIC,
            ) {
                SegmentedButtonWithTitle(
                    title = stringResource(id = R.string.theme_contrast_option),
                    locked = mainState.value.theme != Theme.DYNAMIC,
                    buttons = ThemeContrast.entries.map {
                        ButtonItem(
                            it.toString(),
                            when (it) {
                                ThemeContrast.STANDARD -> stringResource(id = R.string.theme_contrast_standard)
                                ThemeContrast.MEDIUM -> stringResource(id = R.string.theme_contrast_medium)
                                ThemeContrast.HIGH -> stringResource(id = R.string.theme_contrast_high)
                            },
                            MaterialTheme.typography.labelLarge,
                            it == mainState.value.themeContrast
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

    item {
        SlidingTransition(
            modifier = Modifier.animateItem(
                fadeInSpec = null,
                fadeOutSpec = null
            ),
            visible = mainState.value.darkTheme!!.isDark(),
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
                        it == mainState.value.pureDark
                    )
                }
            ) {
                onMainEvent(
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
                .height(8.dp)
        )
    }
}