package ua.acclorite.book_story

import android.annotation.SuppressLint
import android.database.CursorWindow
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint
import ua.acclorite.book_story.presentation.components.bottom_navigation_bar.BottomNavigationBar
import ua.acclorite.book_story.presentation.components.custom_navigation_rail.CustomNavigationRail
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.data.NavigationHost
import ua.acclorite.book_story.presentation.data.Screen
import ua.acclorite.book_story.presentation.screens.about.AboutScreenRoot
import ua.acclorite.book_story.presentation.screens.about.data.AboutViewModel
import ua.acclorite.book_story.presentation.screens.about.nested.credits.CreditsScreenRoot
import ua.acclorite.book_story.presentation.screens.about.nested.license_info.LicenseInfoScreenRoot
import ua.acclorite.book_story.presentation.screens.about.nested.license_info.data.LicenseInfoViewModel
import ua.acclorite.book_story.presentation.screens.about.nested.licenses.LicensesScreenRoot
import ua.acclorite.book_story.presentation.screens.about.nested.licenses.data.LicensesViewModel
import ua.acclorite.book_story.presentation.screens.book_info.BookInfoScreenRoot
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoViewModel
import ua.acclorite.book_story.presentation.screens.browse.BrowseScreenRoot
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.presentation.screens.help.HelpScreenRoot
import ua.acclorite.book_story.presentation.screens.help.data.HelpViewModel
import ua.acclorite.book_story.presentation.screens.history.HistoryScreenRoot
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.LibraryScreenRoot
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.screens.reader.ReaderScreenRoot
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderViewModel
import ua.acclorite.book_story.presentation.screens.settings.SettingsScreenRoot
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsViewModel
import ua.acclorite.book_story.presentation.screens.settings.nested.appearance.AppearanceSettingsRoot
import ua.acclorite.book_story.presentation.screens.settings.nested.general.GeneralSettingsRoot
import ua.acclorite.book_story.presentation.screens.settings.nested.reader.ReaderSettingsRoot
import ua.acclorite.book_story.presentation.screens.start.StartScreenRoot
import ua.acclorite.book_story.presentation.screens.start.data.StartViewModel
import ua.acclorite.book_story.presentation.ui.BookStoryTheme
import ua.acclorite.book_story.presentation.ui.Transitions
import ua.acclorite.book_story.presentation.ui.isDark
import ua.acclorite.book_story.presentation.ui.isPureDark
import java.lang.reflect.Field


@Suppress("unused")
@SuppressLint("DiscouragedPrivateApi")
@AndroidEntryPoint
class Activity : AppCompatActivity() {
    // Initializing all viewModels on app startup
    private val mainViewModel: MainViewModel by viewModels()
    private val libraryViewModel: LibraryViewModel by viewModels()
    private val historyViewModel: HistoryViewModel by viewModels()
    private val browseViewModel: BrowseViewModel by viewModels()
    private val bookInfoViewModel: BookInfoViewModel by viewModels()
    private val readerViewModel: ReaderViewModel by viewModels()
    private val helpViewModel: HelpViewModel by viewModels()
    private val aboutViewModel: AboutViewModel by viewModels()
    private val licenseInfoViewModel: LicenseInfoViewModel by viewModels()
    private val licensesViewModel: LicensesViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()
    private val startViewModel: StartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // New Cursor size for Room
        try {
            val field: Field = CursorWindow::class.java.getDeclaredField("sCursorWindowSize")
            field.isAccessible = true
            field.set(null, 100 * 1024 * 1024)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Initializing all variables
        mainViewModel.init(libraryViewModel)

        // Splash screen
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainViewModel.isReady.value
            }
        }

        // Edge to edge
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            val isLoaded by mainViewModel.isReady.collectAsState()
            val state by mainViewModel.state.collectAsState()

            val density = LocalDensity.current
            val imeInsets = WindowInsets.ime
            val focusManager = LocalFocusManager.current
            val isKeyboardVisible by remember {
                derivedStateOf {
                    imeInsets.getBottom(density) > 0
                }
            }

            LaunchedEffect(isKeyboardVisible) {
                if (!isKeyboardVisible) {
                    focusManager.clearFocus()
                }
            }

            if (isLoaded) {
                BookStoryTheme(
                    theme = state.theme!!,
                    isDark = state.darkTheme!!.isDark(),
                    isPureDark = state.pureDark!!.isPureDark(this),
                    themeContrast = state.themeContrast!!
                ) {
                    NavigationHost(
                        startScreen = if (state.showStartScreen!!) Screen.START
                        else Screen.LIBRARY
                    ) {
                        navigation(
                            screens = arrayOf(Screen.LIBRARY, Screen.HISTORY, Screen.BROWSE),
                            bottomBar = {
                                BottomNavigationBar()
                            },
                            navigationRail = {
                                CustomNavigationRail()
                            }
                        ) {
                            // Library
                            composable(
                                screen = Screen.LIBRARY,
                                enterAnim = Transitions.FadeTransitionIn,
                                exitAnim = Transitions.FadeTransitionOut
                            ) {
                                LibraryScreenRoot()
                            }

                            // History
                            composable(
                                screen = Screen.HISTORY,
                                enterAnim = Transitions.FadeTransitionIn,
                                exitAnim = Transitions.FadeTransitionOut
                            ) {
                                HistoryScreenRoot()
                            }

                            // Browse
                            composable(
                                screen = Screen.BROWSE,
                                enterAnim = Transitions.FadeTransitionIn,
                                exitAnim = Transitions.FadeTransitionOut
                            ) {
                                BrowseScreenRoot()
                            }
                        }

                        // Book Info
                        composable(screen = Screen.BOOK_INFO) {
                            BookInfoScreenRoot()
                        }

                        // Reader
                        composable(screen = Screen.READER) {
                            ReaderScreenRoot()
                        }

                        // Settings
                        composable(screen = Screen.SETTINGS) {
                            SettingsScreenRoot()
                        }

                        // Nested settings categories
                        composable(screen = Screen.GENERAL_SETTINGS) {
                            GeneralSettingsRoot()
                        }
                        composable(screen = Screen.APPEARANCE_SETTINGS) {
                            AppearanceSettingsRoot()
                        }
                        composable(screen = Screen.READER_SETTINGS) {
                            ReaderSettingsRoot()
                        }

                        // About screen
                        composable(screen = Screen.ABOUT) {
                            AboutScreenRoot()
                        }

                        // Nested about categories
                        composable(screen = Screen.LICENSES) {
                            LicensesScreenRoot()
                        }
                        composable(screen = Screen.LICENSES_INFO) {
                            LicenseInfoScreenRoot()
                        }
                        composable(screen = Screen.CREDITS) {
                            CreditsScreenRoot()
                        }

                        // Help screen
                        composable(screen = Screen.HELP) {
                            HelpScreenRoot()
                        }

                        // Start screen
                        composable(
                            screen = Screen.START,
                            enterAnim = Transitions.FadeTransitionIn
                        ) {
                            StartScreenRoot()
                        }
                    }
                }
            }
        }
    }
}