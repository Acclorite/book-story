package ua.acclorite.book_story.presentation.components.custom_navigation_rail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.domain.util.Route
import ua.acclorite.book_story.presentation.data.LocalNavigator

/**
 * Custom Navigation Rail. It is used to be shown on Tablets.
 */
@Composable
fun BoxScope.CustomNavigationRail() {
    var currentScreen: Route? by remember { mutableStateOf(null) }
    val navigator = LocalNavigator.current
    val layoutDirection = LocalLayoutDirection.current

    LaunchedEffect(Unit) {
        navigator.currentScreen.collect { route ->
            if (
                Constants.NAVIGATION_ITEMS.any {
                    navigator.run { it.screen.getRoute() } == route
                }
            ) {
                currentScreen = route
            }
        }
    }

    NavigationRail(
        modifier = Modifier
            .align(
                Alignment.CenterStart
            )
            .background(MaterialTheme.colorScheme.surfaceContainer)
            .padding(
                start = WindowInsets.navigationBars
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
            Constants.NAVIGATION_ITEMS.forEach {
                CustomNavigationRailItem(
                    item = it,
                    isSelected = currentScreen == navigator.run { it.screen.getRoute() }
                ) {
                    navigator.navigate(it.screen, false)
                }
            }
        }
    }
}