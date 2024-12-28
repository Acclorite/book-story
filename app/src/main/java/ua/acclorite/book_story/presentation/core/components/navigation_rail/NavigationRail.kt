package ua.acclorite.book_story.presentation.core.components.navigation_rail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.domain.navigator.NavigatorItem
import ua.acclorite.book_story.presentation.navigator.LocalNavigator

@Composable
fun NavigationRail(tabs: List<NavigatorItem>) {
    val navigator = LocalNavigator.current
    val layoutDirection = LocalLayoutDirection.current
    val lastItem = navigator.lastItem.collectAsStateWithLifecycle()

    val currentTab = remember { mutableStateOf(lastItem.value) }
    LaunchedEffect(lastItem.value) {
        if (tabs.any { it.screen::class == lastItem.value::class }) {
            currentTab.value = lastItem.value
        }
    }

    NavigationRail(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(
                start = WindowInsets.navigationBars
                    .asPaddingValues()
                    .calculateStartPadding(layoutDirection) +
                        WindowInsets.displayCutout
                            .asPaddingValues()
                            .calculateStartPadding(layoutDirection)
            )
            .width(80.dp)
            .padding(horizontal = 12.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
        windowInsets = WindowInsets(0, 0, 0, 0)
    ) {
        Column(
            Modifier
                .fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            tabs.forEach { tab ->
                NavigationRailItem(
                    item = tab,
                    isSelected = currentTab.value::class == tab.screen::class
                ) {
                    navigator.push(tab.screen)
                }
            }
        }
    }
}