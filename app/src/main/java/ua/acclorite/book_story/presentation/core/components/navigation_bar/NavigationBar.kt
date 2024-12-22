package ua.acclorite.book_story.presentation.core.components.navigation_bar

import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.domain.navigator.NavigatorItem
import ua.acclorite.book_story.presentation.navigator.LocalNavigator

@Composable
fun NavigationBar(tabs: List<NavigatorItem>) {
    val navigator = LocalNavigator.current
    val currentTab = navigator.lastItem.collectAsStateWithLifecycle()

    NavigationBar {
        tabs.forEach { tab ->
            NavigationBarItem(
                item = tab,
                isSelected = currentTab.value::class == tab.screen::class
            ) {
                navigator.push(tab.screen)
            }
        }
    }
}