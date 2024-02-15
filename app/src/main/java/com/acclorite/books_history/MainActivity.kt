package com.acclorite.books_history

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.acclorite.books_history.domain.model.Book
import com.acclorite.books_history.presentation.NavigationHost
import com.acclorite.books_history.presentation.Screen
import com.acclorite.books_history.presentation.components.BottomNavigationBar
import com.acclorite.books_history.presentation.main.MainEvent
import com.acclorite.books_history.presentation.main.MainViewModel
import com.acclorite.books_history.presentation.screens.browse.BrowseScreen
import com.acclorite.books_history.presentation.screens.library.LibraryScreen
import com.acclorite.books_history.presentation.screens.settings.SettingsScreen
import com.acclorite.books_history.presentation.screens.settings.nested.appearance.AppearanceSettings
import com.acclorite.books_history.presentation.screens.settings.nested.general.GeneralSettings
import com.acclorite.books_history.ui.BooksHistoryResurrectionTheme
import com.acclorite.books_history.ui.DefaultTransition
import com.acclorite.books_history.ui.Transitions
import com.acclorite.books_history.ui.isDark
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import dagger.hilt.android.AndroidEntryPoint

@OptIn(ExperimentalPermissionsApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.init(this)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainViewModel.isReady.value
            }
        }
        enableEdgeToEdge()

        setContent {
            val updating = mainViewModel.updating.collectAsState().value
            val theme = mainViewModel.theme.collectAsState().value!!
            val darkTheme = mainViewModel.darkTheme.collectAsState().value!!

            BooksHistoryResurrectionTheme(
                theme = theme,
                isDark = darkTheme.isDark()
            ) {
                if (!updating) {
                    NavigationHost(startScreen = Screen.LIBRARY) {
                        val currentScreen by this.getCurrentScreen().collectAsState()
                        DefaultTransition(
                            visible = currentScreen == Screen.LIBRARY ||
                                    currentScreen == Screen.HISTORY ||
                                    currentScreen == Screen.BROWSE
                        ) {
                            Scaffold(
                                bottomBar = {
                                    BottomNavigationBar(navigator = this)
                                },
                                containerColor = MaterialTheme.colorScheme.surface
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .padding(bottom = it.calculateBottomPadding())
                                ) {
                                    composable(screen = Screen.LIBRARY) {
                                        @Suppress("UNCHECKED_CAST")
                                        LibraryScreen(
                                            navigator = this@NavigationHost,
                                            addedBooks = retrieveArgument("added_books") as? List<Book>
                                                ?: emptyList()
                                        )
                                    }

                                    composable(screen = Screen.BROWSE) {
                                        BrowseScreen(navigator = this@NavigationHost)
                                    }
                                }
                            }
                        }

                        // Settings
                        composable(
                            screen = Screen.SETTINGS,
                            enterAnim = Transitions.SlidingTransitionIn,
                            exitAnim = Transitions.SlidingTransitionOut
                        ) {
                            SettingsScreen(
                                viewModel = mainViewModel,
                                navigator = this@NavigationHost
                            )
                        }

                        composable(
                            screen = Screen.GENERAL_SETTINGS,
                            enterAnim = Transitions.SlidingTransitionIn,
                            exitAnim = Transitions.SlidingTransitionOut
                        ) {
                            GeneralSettings(
                                mainViewModel = mainViewModel,
                                navigator = this@NavigationHost
                            )
                        }
                        composable(
                            screen = Screen.APPEARANCE_SETTINGS,
                            enterAnim = Transitions.SlidingTransitionIn,
                            exitAnim = Transitions.SlidingTransitionOut
                        ) {
                            AppearanceSettings(
                                mainViewModel = mainViewModel,
                                navigator = this@NavigationHost
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                if (darkTheme.isDark()) Color.Black
                                else Color.White
                            )
                    )
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        mainViewModel.onEvent(MainEvent.OnLocaleUpdate(this))
    }
}