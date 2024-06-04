package ua.acclorite.book_story.presentation.components.bottom_navigation_bar

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.Screen

/**
 * Bottom navigation bar, uses default [NavigationBar].
 */
@Composable
fun BottomNavigationBar() {
    var currentScreen: Screen? by remember { mutableStateOf(null) }
    val navigator = LocalNavigator.current

    LaunchedEffect(Unit) {
        navigator.getCurrentScreen().collect {
            if (it == Screen.LIBRARY || it == Screen.HISTORY || it == Screen.BROWSE) {
                currentScreen = it
            }
        }
    }

    NavigationBar {
        Constants.NAVIGATION_ITEMS.forEach {
            BottomNavigationBarItem(
                item = it,
                isSelected = currentScreen == it.screen
            ) {
                navigator.navigate(it.screen, false)
            }
        }
    }
}
