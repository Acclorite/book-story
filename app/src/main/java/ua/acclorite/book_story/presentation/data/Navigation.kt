package ua.acclorite.book_story.presentation.data

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.viewModels
import androidx.annotation.Keep
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import ua.acclorite.book_story.domain.util.Route
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.ui.Transitions

private const val CURRENT_SCREEN = "current_screen"
private const val BACKSTACK = "back_stack"
private const val USE_BACK_ANIM = "use_back_animation"
private const val SCREENS = "screens"

/**
 * Passed in [CompositionLocalProvider] and can be accessed through [LocalNavigator].current.
 */
val LocalNavigator = compositionLocalOf<Navigator> {
    error("Cannot initialize Navigator.")
}

/**
 * Screens. Should be [Serializable] to be able to pass them inside [SavedStateHandle].
 * Each screen should have unique name.
 */
@Immutable
@Parcelize
@Keep
sealed class Screen : Parcelable {

    @Parcelize
    @Keep
    data object Library : Screen()

    @Parcelize
    @Keep
    data object History : Screen()

    @Parcelize
    @Keep
    data object Browse : Screen()

    @Parcelize
    @Keep
    data class BookInfo(
        val bookId: Int
    ) : Screen()

    @Parcelize
    @Keep
    data class Reader(
        val bookId: Int
    ) : Screen()

    @Parcelize
    @Keep
    data object Settings : Screen() {
        @Parcelize
        @Keep
        data object General : Screen()

        @Parcelize
        @Keep
        data object Appearance : Screen()

        @Parcelize
        @Keep
        data object ReaderSettings : Screen()

        @Parcelize
        @Keep
        data object BrowseSettings : Screen()
    }

    @Parcelize
    @Keep
    data object About : Screen() {
        @Parcelize
        @Keep
        data object Licenses : Screen()

        @Parcelize
        @Keep
        data class LicenseInfo(val licenseId: String) : Screen()

        @Parcelize
        @Keep
        data object Credits : Screen()
    }

    @Parcelize
    @Keep
    data class Help(
        val fromStart: Boolean
    ) : Screen()

    @Parcelize
    @Keep
    data object Start : Screen()
}

/**
 * Navigator. Used to navigate between screens.
 *
 * [Navigator.currentScreen] param represents current [Screen].
 * [Navigator.navigate] navigates to [Screen] passed as param.
 */
@HiltViewModel(assistedFactory = Navigator.Factory::class)
class Navigator @AssistedInject constructor(
    private val savedStateHandle: SavedStateHandle,
    @Assisted private val startScreen: Screen
) : ViewModel() {

    val currentScreen = savedStateHandle.getStateFlow(CURRENT_SCREEN, startScreen.getRoute())
    val useBackAnimation = savedStateHandle.getStateFlow(USE_BACK_ANIM, false)
    private val backStack = savedStateHandle.getStateFlow(BACKSTACK, mutableListOf<Route>())
    val screens = savedStateHandle.getStateFlow(SCREENS, mutableListOf<Screen>())

    init {
        putScreen(startScreen)
    }

    /**
     * Saves screen into [screens]. Later can be retrieved via [retrieveScreen].
     *
     * @param screen [Screen].
     */
    fun putScreen(screen: Screen) {
        var found = false

        for ((index, arg) in screens.value.withIndex()) {
            if (arg.getRoute() == screen.getRoute()) {
                screens.value[index] = screen
                found = true
                break
            }
        }

        if (!found) {
            screens.value.add(screen)
        }
    }

    /**
     * Retrieves screen that was put via [putScreen].
     *
     * @exception ClassCastException throws an exception if could not cast saved screen as [S].
     * @exception Exception if there is no such screen saved that specified in [S], throws an [Exception].
     */
    inline fun <reified S : Screen> retrieveScreen(): S {
        for (arg in screens.value) {
            if (arg.getRoute() == getRoute<S>()) {
                val screen = arg as? S
                    ?: throw ClassCastException("Cannot cast ${arg::class} as ${S::class}")
                return screen
            }
        }

        throw Exception("Screen was not found.")
    }

    /**
     * Navigates to the desired screen. Ignored if [currentScreen] is already [screen].
     *
     * @param screen [Screen] to navigate to.
     * @param useBackAnimation Whether back animation should be used(as when user goes back).
     * @param saveInBackStack Whether this screen should be saved in [backStack] (basically history of all opened screens).
     */
    fun navigate(
        screen: Screen,
        useBackAnimation: Boolean = false,
        saveInBackStack: Boolean = true
    ) = viewModelScope.launch(Dispatchers.Default) {
        if (screen.getRoute() == currentScreen.value) {
            return@launch
        }

        if (saveInBackStack) {
            backStack.value.add(currentScreen.value)
        }

        putScreen(screen)

        savedStateHandle[USE_BACK_ANIM] = useBackAnimation
        savedStateHandle[CURRENT_SCREEN] = screen.getRoute()
    }

    /**
     * Navigates user to the previous screen, if there is.
     * If there is nowhere to go, this call is ignored.
     *
     * @param useBackAnimation Whether user should see back animation when he goes to the previous screen.
     */
    fun navigateBack(
        useBackAnimation: Boolean = true
    ) = viewModelScope.launch(Dispatchers.Default) {
        if (canGoBack()) {
            savedStateHandle[USE_BACK_ANIM] = useBackAnimation
            savedStateHandle[CURRENT_SCREEN] = backStack.value.last()

            backStack.value.removeLast()
        }
    }

    /**
     * Clears the whole [backStack].
     */
    fun clearBackStack() {
        backStack.value.clear()
    }

    /**
     * Whether there is screen to go back.
     */
    fun canGoBack(): Boolean {
        return backStack.value.isNotEmpty()
    }

    /**
     * Gets route of specified [S].
     */
    inline fun <reified S : Screen> getRoute(): Route {
        return S::class.simpleName!!
    }

    /**
     * Gets route of this screen.
     */
    fun Screen.getRoute(): Route {
        return this::class.simpleName!!
    }

    /**
     * Animated Screen. Used in [NavigationHost].
     * Each [composable] should have unique [Screen].
     *
     * @param enterAnim Enter Animation.
     * @param backEnterAnim Enter Animation for navigating back.
     * @param exitAnim Exit Animation.
     * @param backExitAnim Exit Animation for navigating back.
     * @param content The Screen content to show when [Navigator.currentScreen] equals [S].
     */
    @SuppressLint("ComposableNaming")
    @Composable
    inline fun <reified S : Screen> composable(
        enterAnim: EnterTransition = Transitions.SlidingTransitionIn,
        backEnterAnim: EnterTransition = Transitions.BackSlidingTransitionIn,
        exitAnim: ExitTransition = Transitions.SlidingTransitionOut,
        backExitAnim: ExitTransition = Transitions.BackSlidingTransitionOut,
        noinline content: @Composable (screen: S) -> Unit
    ) {
        val currentRoute = currentScreen.collectAsState()
        val useBackAnimation by useBackAnimation.collectAsState()
        val shouldShow by remember {
            derivedStateOf {
                currentRoute.value == getRoute<S>()
            }
        }

        CustomAnimatedVisibility(
            visible = shouldShow,
            enter = if (!useBackAnimation) enterAnim else backEnterAnim,
            exit = if (!useBackAnimation) exitAnim else backExitAnim
        ) {
            val screen = remember { retrieveScreen<S>() }
            content(screen)
        }
    }

    /**
     * Navigator's navigation.
     * Shows BottomBar or Navigation Rail when any of the [screens] are currently showing.
     *
     * @param screens [Screen]s where navigation shows.
     * @param enterBarAnim Enter animation for navigation.
     * @param backEnterBarAnim Back enter animation for navigation.
     * @param exitBarAnim Exit animation for navigation.
     * @param backExitBarAnim Back exit animation for navigation.
     * @param bottomBar Bottom bar, sticks to the bottom.
     * @param navigationRail Navigation rail, sticks to the sides.
     * @param content Content, all [screens] should be inside as [composable].
     */
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    @SuppressLint("ComposableNaming")
    @Composable
    fun navigation(
        vararg screens: Route,
        enterBarAnim: EnterTransition = Transitions.SlidingTransitionIn,
        backEnterBarAnim: EnterTransition = Transitions.BackSlidingTransitionIn,
        exitBarAnim: ExitTransition = Transitions.SlidingTransitionOut,
        backExitBarAnim: ExitTransition = Transitions.BackSlidingTransitionOut,
        bottomBar: @Composable () -> Unit,
        navigationRail: @Composable BoxScope.() -> Unit,
        content: @Composable () -> Unit
    ) {
        val activity = LocalContext.current as ComponentActivity
        val currentScreen = currentScreen.collectAsState()
        val useBackAnimation by useBackAnimation.collectAsState()
        val shouldShow by remember {
            derivedStateOf {
                screens.any { it == currentScreen.value }
            }
        }

        val windowClass = calculateWindowSizeClass(activity = activity)
        val tabletUI = remember(windowClass) {
            windowClass.widthSizeClass != WindowWidthSizeClass.Compact
        }
        val layoutDirection = LocalLayoutDirection.current

        CustomAnimatedVisibility(
            visible = shouldShow,
            enter = if (useBackAnimation) backEnterBarAnim
            else enterBarAnim,
            exit = if (useBackAnimation) backExitBarAnim
            else exitBarAnim
        ) {
            Scaffold(
                bottomBar = {
                    if (!tabletUI) {
                        bottomBar()
                    }
                },
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(
                                end = it.calculateEndPadding(
                                    layoutDirection
                                ),
                                bottom = it.calculateBottomPadding(),
                                start = if (tabletUI) {
                                    80.dp + it.calculateStartPadding(
                                        layoutDirection
                                    )
                                } else {
                                    it.calculateStartPadding(
                                        layoutDirection
                                    )
                                },
                            )
                    ) {
                        content()
                    }

                    if (tabletUI) {
                        navigationRail()
                    }
                }
            }
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(startScreen: Screen): Navigator
    }
}

/**
 * Custom Navigation Host. Contains [Navigator.composable]s in [content].
 * Based on [CustomAnimatedVisibility].
 *
 * @param startScreen Start Screen. Be sure to pass [Screen] that uses in one of your [Navigator.composable]s.
 * @param colorBetweenAnimations The color, that using between animations, recommended to set this to background or navigation bar color.
 * @param content Content of the [NavigationHost]. Highly recommended to use [Navigator.composable].
 */
@Composable
fun NavigationHost(
    startScreen: Screen,
    colorBetweenAnimations: Color = MaterialTheme.colorScheme.surface,
    content: @Composable Navigator.() -> Unit
) {
    val activity = LocalContext.current as ComponentActivity
    val navigator by activity.viewModels<Navigator>(
        extrasProducer = {
            activity
                .defaultViewModelCreationExtras
                .withCreationCallback<Navigator.Factory> { factory ->
                    factory.create(startScreen)
                }
        }
    )

    BackHandler {
        if (navigator.canGoBack()) {
            navigator.navigateBack()
        } else {
            activity.finishAffinity()
        }
    }

    Box(
        Modifier
            .fillMaxSize()
            .background(colorBetweenAnimations)
    )

    CompositionLocalProvider(LocalNavigator provides navigator) {
        content(navigator)
    }
}