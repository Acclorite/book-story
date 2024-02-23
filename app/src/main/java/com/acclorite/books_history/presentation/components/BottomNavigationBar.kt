package com.acclorite.books_history.presentation.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.acclorite.books_history.R
import com.acclorite.books_history.domain.model.NavigationBarItem
import com.acclorite.books_history.presentation.Navigator
import com.acclorite.books_history.presentation.Screen
import com.acclorite.books_history.ui.elevation

/**
 * Bottom navigation bar, uses default [NavigationBar].
 */
@Composable
fun BottomNavigationBar(
    navigator: Navigator
) {
    val currentScreen by navigator.getCurrentScreen().collectAsState()

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
