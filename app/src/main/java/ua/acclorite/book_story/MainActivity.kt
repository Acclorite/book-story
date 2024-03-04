package ua.acclorite.book_story

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import ua.acclorite.book_story.domain.model.Book
import ua.acclorite.book_story.presentation.NavigationHost
import ua.acclorite.book_story.presentation.Screen
import ua.acclorite.book_story.presentation.components.BottomNavigationBar
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.screens.book_info.BookInfoScreen
import ua.acclorite.book_story.presentation.screens.browse.BrowseScreen
import ua.acclorite.book_story.presentation.screens.history.HistoryScreen
import ua.acclorite.book_story.presentation.screens.library.LibraryScreen
import ua.acclorite.book_story.presentation.screens.reader.ReaderScreen
import ua.acclorite.book_story.presentation.screens.settings.SettingsScreen
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.AppearanceSettings
import ua.acclorite.book_story.presentation.screens.settings.nested.general.GeneralSettings
import ua.acclorite.book_story.ui.BooksHistoryResurrectionTheme
import ua.acclorite.book_story.ui.Transitions
import ua.acclorite.book_story.ui.isDark

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
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
        WindowCompat.setDecorFitsSystemWindows(window, false)

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
                        AnimatedVisibility(
                            visible = currentScreen == Screen.LIBRARY ||
                                    currentScreen == Screen.HISTORY ||
                                    currentScreen == Screen.BROWSE,
                            enter = fadeIn(tween(200)) +
                                    slideInHorizontally(tween(200)) { it / 15 },
                            exit = Transitions.SlidingTransitionOut
                        ) {
                            Scaffold(
                                bottomBar = {
                                    BottomNavigationBar(navigator = this@NavigationHost)
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

                                    composable(screen = Screen.HISTORY) {
                                        HistoryScreen(navigator = this@NavigationHost)
                                    }

                                    composable(screen = Screen.BROWSE) {
                                        BrowseScreen(navigator = this@NavigationHost)
                                    }
                                }
                            }
                        }

                        // Book Info
                        composable(
                            screen = Screen.BOOK_INFO,
                            enterAnim = Transitions.SlidingTransitionIn,
                            exitAnim = Transitions.SlidingTransitionOut
                        ) {
                            BookInfoScreen(
                                navigator = this@NavigationHost
                            )
                        }
                        composable(
                            screen = Screen.READER,
                            enterAnim = Transitions.SlidingTransitionIn,
                            exitAnim = Transitions.SlidingTransitionOut
                        ) {
                            ReaderScreen(
                                navigator = this@NavigationHost
                            )
                        }

                        // Settings
                        composable(
                            screen = Screen.SETTINGS,
                            enterAnim = Transitions.SlidingTransitionIn,
                            exitAnim = Transitions.SlidingTransitionOut
                        ) {
                            SettingsScreen(
                                navigator = this@NavigationHost
                            )
                        }

                        // Nested categories
                        composable(
                            screen = Screen.GENERAL_SETTINGS,
                            enterAnim = Transitions.SlidingTransitionIn,
                            exitAnim = Transitions.SlidingTransitionOut
                        ) {
                            GeneralSettings(
                                navigator = this@NavigationHost
                            )
                        }
                        composable(
                            screen = Screen.APPEARANCE_SETTINGS,
                            enterAnim = Transitions.SlidingTransitionIn,
                            exitAnim = Transitions.SlidingTransitionOut
                        ) {
                            AppearanceSettings(
                                navigator = this@NavigationHost
                            )
                        }
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                MaterialTheme.colorScheme.surface
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