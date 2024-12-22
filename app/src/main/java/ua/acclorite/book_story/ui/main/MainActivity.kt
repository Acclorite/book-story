@file:Suppress("UnusedVariable", "unused")

package ua.acclorite.book_story.ui.main

import android.annotation.SuppressLint
import android.database.CursorWindow
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.togetherWith
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.internal.immutableListOf
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.navigator.NavigatorItem
import ua.acclorite.book_story.domain.navigator.StackEvent
import ua.acclorite.book_story.presentation.core.components.navigation_bar.NavigationBar
import ua.acclorite.book_story.presentation.core.components.navigation_rail.NavigationRail
import ua.acclorite.book_story.presentation.main.MainActivityKeyboardManager
import ua.acclorite.book_story.presentation.navigator.Navigator
import ua.acclorite.book_story.presentation.navigator.NavigatorTabs
import ua.acclorite.book_story.ui.browse.BrowseModel
import ua.acclorite.book_story.ui.browse.BrowseScreen
import ua.acclorite.book_story.ui.history.HistoryModel
import ua.acclorite.book_story.ui.history.HistoryScreen
import ua.acclorite.book_story.ui.library.LibraryModel
import ua.acclorite.book_story.ui.library.LibraryScreen
import ua.acclorite.book_story.ui.settings.SettingsModel
import ua.acclorite.book_story.ui.start.StartScreen
import ua.acclorite.book_story.ui.theme.BookStoryTheme
import ua.acclorite.book_story.ui.theme.Transitions
import ua.acclorite.book_story.ui.theme.isDark
import ua.acclorite.book_story.ui.theme.isPureDark
import java.lang.reflect.Field


@SuppressLint("DiscouragedPrivateApi")
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    // Creating an instance of Models
    private val mainModel: MainModel by viewModels()
    private val settingsModel: SettingsModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        // Splash screen
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainModel.isReady.value
            }
        }

        // Default super
        super.onCreate(savedInstanceState)

        // Bigger Cursor size for Room
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 100 * 1024 * 1024)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Initializing the MainModel
        mainModel.init(settingsModel.isReady)

        // Edge to edge
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            // Initializing Screen Models
            val libraryModel = hiltViewModel<LibraryModel>()
            val historyModel = hiltViewModel<HistoryModel>()
            val browseModel = hiltViewModel<BrowseModel>()

            val state = mainModel.state.collectAsStateWithLifecycle()
            val isLoaded = mainModel.isReady.collectAsStateWithLifecycle()

            val tabs = immutableListOf(
                NavigatorItem(
                    screen = LibraryScreen,
                    title = R.string.library_screen,
                    tooltip = R.string.library_content_desc,
                    selectedIcon = R.drawable.library_screen_filled,
                    unselectedIcon = R.drawable.library_screen_outlined
                ),
                NavigatorItem(
                    screen = HistoryScreen,
                    title = R.string.history_screen,
                    tooltip = R.string.history_content_desc,
                    selectedIcon = R.drawable.history_screen_filled,
                    unselectedIcon = R.drawable.history_screen_outlined
                ),
                NavigatorItem(
                    screen = BrowseScreen,
                    title = R.string.browse_screen,
                    tooltip = R.string.browse_content_desc,
                    selectedIcon = R.drawable.browse_screen_filled,
                    unselectedIcon = R.drawable.browse_screen_outlined
                )
            )

            MainActivityKeyboardManager()

            if (isLoaded.value) {
                BookStoryTheme(
                    theme = state.value.theme,
                    isDark = state.value.darkTheme.isDark(),
                    isPureDark = state.value.pureDark.isPureDark(this),
                    themeContrast = state.value.themeContrast
                ) {
                    Navigator(
                        initialScreen = if (state.value.showStartScreen) StartScreen
                        else LibraryScreen,
                        transitionSpec = { lastEvent ->
                            when (lastEvent) {
                                StackEvent.Default -> {
                                    Transitions.SlidingTransitionIn
                                        .togetherWith(Transitions.SlidingTransitionOut)
                                }

                                StackEvent.Pop -> {
                                    Transitions.BackSlidingTransitionIn
                                        .togetherWith(Transitions.BackSlidingTransitionOut)
                                }
                            }
                        },
                        contentKey = {
                            when (it) {
                                LibraryScreen, HistoryScreen, BrowseScreen -> "tabs"
                                else -> it
                            }
                        },
                        backHandlerEnabled = { it != StartScreen }
                    ) { screen ->
                        when (screen) {
                            LibraryScreen, HistoryScreen, BrowseScreen -> {
                                NavigatorTabs(
                                    currentTab = screen,
                                    transitionSpec = {
                                        Transitions.FadeTransitionIn
                                            .togetherWith(Transitions.FadeTransitionOut)
                                    },
                                    navigationBar = { NavigationBar(tabs = tabs) },
                                    navigationRail = { NavigationRail(tabs = tabs) }
                                ) { tab ->
                                    tab.Content()
                                }
                            }

                            else -> {
                                screen.Content()
                            }
                        }
                    }
                }
            }
        }
    }
}