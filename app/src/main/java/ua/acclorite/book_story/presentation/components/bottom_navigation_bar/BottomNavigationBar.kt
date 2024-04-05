package ua.acclorite.book_story.presentation.components.bottom_navigation_bar

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.NavigationItem
import ua.acclorite.book_story.presentation.data.Navigator
import ua.acclorite.book_story.presentation.data.Screen

/**
 * Bottom navigation bar, uses default [NavigationBar].
 *
 * @param navigator Navigator.
 */
@Composable
fun BottomNavigationBar(
    navigator: Navigator
) {
    var currentScreen: Screen? by remember { mutableStateOf(null) }

    LaunchedEffect(Unit) {
        navigator.getCurrentScreen().collect {
            if (it == Screen.LIBRARY || it == Screen.HISTORY || it == Screen.BROWSE) {
                currentScreen = it
            }
        }
    }

    NavigationBar {

        BottomNavigationBarItem(
            item = NavigationItem(
                stringResource(id = R.string.library_screen),
                selectedIcon = painterResource(id = R.drawable.library_screen_filled),
                unselectedIcon = painterResource(id = R.drawable.library_screen_outlined)
            ),
            tooltipText = stringResource(id = R.string.library_content_desc),
            isSelected = currentScreen == Screen.LIBRARY
        ) {
            navigator.navigate(Screen.LIBRARY, false)
        }

        BottomNavigationBarItem(
            item = NavigationItem(
                stringResource(id = R.string.history_screen),
                selectedIcon = painterResource(id = R.drawable.history_screen_filled),
                unselectedIcon = painterResource(id = R.drawable.history_screen_outlined)
            ),
            tooltipText = stringResource(id = R.string.history_content_desc),
            isSelected = currentScreen == Screen.HISTORY
        ) {
            navigator.navigate(Screen.HISTORY, false)
        }

        BottomNavigationBarItem(
            item = NavigationItem(
                stringResource(id = R.string.browse_screen),
                selectedIcon = painterResource(id = R.drawable.browse_screen_filled),
                unselectedIcon = painterResource(id = R.drawable.browse_screen_outlined)
            ),
            tooltipText = stringResource(id = R.string.browse_content_desc),
            isSelected = currentScreen == Screen.BROWSE
        ) {
            navigator.navigate(Screen.BROWSE, false)
        }
    }
}
