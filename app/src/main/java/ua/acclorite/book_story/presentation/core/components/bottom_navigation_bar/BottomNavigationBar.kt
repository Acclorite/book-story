package ua.acclorite.book_story.presentation.core.components.bottom_navigation_bar

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import ua.acclorite.book_story.domain.util.Route
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideNavigationItems
import ua.acclorite.book_story.presentation.core.navigation.LocalNavigatorInstance

/**
 * Bottom navigation bar, uses default [NavigationBar].
 */
@Composable
fun BottomNavigationBar() {
    var currentScreen: Route? by remember { mutableStateOf(null) }
    val navigator = LocalNavigatorInstance.current

    LaunchedEffect(Unit) {
        navigator.currentScreen.collect { route ->
            if (
                Constants.provideNavigationItems().any {
                    navigator.run { it.screen.getRoute() } == route
                }
            ) {
                currentScreen = route
            }
        }
    }

    NavigationBar {
        Constants.provideNavigationItems().forEach {
            BottomNavigationBarItem(
                item = it,
                isSelected = currentScreen == navigator.run { it.screen.getRoute() }
            ) {
                navigator.navigate(it.screen, false)
            }
        }
    }
}
