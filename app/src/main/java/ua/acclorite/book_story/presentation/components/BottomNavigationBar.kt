package ua.acclorite.book_story.presentation.components

import androidx.compose.material3.MaterialTheme
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
import ua.acclorite.book_story.domain.model.NavigationBarItem
import ua.acclorite.book_story.presentation.Navigator
import ua.acclorite.book_story.presentation.Screen
import ua.acclorite.book_story.ui.elevation

/**
 * Bottom navigation bar, uses default [NavigationBar].
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

    NavigationBar(
        containerColor = MaterialTheme.elevation()
    ) {
        BottomNavigationBarItem(
            item = NavigationBarItem(
                stringResource(id = R.string.library_screen),
                selectedIcon = painterResource(id = R.drawable.library_screen_filled),
                unselectedIcon = painterResource(id = R.drawable.library_screen_outlined)
            ),
            isSelected = currentScreen == Screen.LIBRARY
        ) {
            navigator.navigate(Screen.LIBRARY)
        }
        BottomNavigationBarItem(
            item = NavigationBarItem(
                stringResource(id = R.string.history_screen),
                selectedIcon = painterResource(id = R.drawable.history_screen_filled),
                unselectedIcon = painterResource(id = R.drawable.history_screen_outlined)
            ),
            isSelected = currentScreen == Screen.HISTORY
        ) {
            navigator.navigate(Screen.HISTORY)
        }
        BottomNavigationBarItem(
            item = NavigationBarItem(
                stringResource(id = R.string.browse_screen),
                selectedIcon = painterResource(id = R.drawable.browse_screen_filled),
                unselectedIcon = painterResource(id = R.drawable.browse_screen_outlined)
            ),
            isSelected = currentScreen == Screen.BROWSE
        ) {
            navigator.navigate(Screen.BROWSE)
        }
    }
}
