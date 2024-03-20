package ua.acclorite.book_story.presentation.data

import android.annotation.SuppressLint
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ua.acclorite.book_story.presentation.ui.Transitions


private const val CURRENT_SCREEN = "current_screen"
private const val BACKSTACK = "back_stack"
private const val USE_BACK_ANIM = "use_back_animation"

/**
 * All screens are listed here, later each screen will be passed as a param for [Navigator.composable] function.
 */
enum class Screen {
    LIBRARY,
    HISTORY,
    BROWSE,

    BOOK_INFO,
    READER,

    SETTINGS,
    GENERAL_SETTINGS,
    APPEARANCE_SETTINGS,
    READER_SETTINGS,

    ABOUT,
    HELP,
    START
}

@Immutable
data class Argument(
    val key: String,
    val arg: Any?
)

/**
 * Navigator. Using for navigation between screens.
 *
 * [Navigator.currentScreen] param represents current [Screen].
 * [Navigator.navigate] navigates to [Screen] passed as param.
 */
@HiltViewModel(assistedFactory = Navigator.Factory::class)
class Navigator @AssistedInject constructor(
    private val savedStateHandle: SavedStateHandle,
    @Assisted startScreen: Screen
) : ViewModel() {

    private val currentScreen = savedStateHandle.getStateFlow(CURRENT_SCREEN, startScreen)
    private val useBackAnimation = savedStateHandle.getStateFlow(USE_BACK_ANIM, false)
    private val backStack = savedStateHandle.getStateFlow(BACKSTACK, mutableListOf<Screen>())
    private val arguments = mutableStateListOf<Argument>()

    fun putArgument(argument: Argument) {
        var found = false

        for ((index, arg) in arguments.withIndex()) {
            if (arg.key == argument.key) {
                arguments[index] = argument
                found = true
                break
            }
        }

        if (!found) {
            arguments.add(argument)
        }
    }

    fun retrieveArgument(key: String): Any? {
        for (arg in arguments) {
            if (arg.key == key) {
                return arg.arg
            }
        }
        return null
    }

    fun clearArgument(key: String) {
        arguments.removeIf { it.key == key }
    }

    fun navigate(screen: Screen, useBackAnimation: Boolean, vararg args: Argument) =
        viewModelScope.launch(Dispatchers.Default) {
            backStack.value.add(currentScreen.value)

            args.forEach {
                putArgument(it)
            }

            savedStateHandle[USE_BACK_ANIM] = useBackAnimation
            savedStateHandle[CURRENT_SCREEN] = screen
        }

    fun navigateWithoutBackStack(screen: Screen, useBackAnimation: Boolean, vararg args: Argument) =
        viewModelScope.launch(Dispatchers.Default) {
            if (backStack.value.last() == screen) {
                backStack.value.removeLast()
            }

            args.forEach {
                putArgument(it)
            }

            savedStateHandle[USE_BACK_ANIM] = useBackAnimation
            savedStateHandle[CURRENT_SCREEN] = screen
        }

    fun navigateBack(useBackAnimation: Boolean = true) =
        viewModelScope.launch(Dispatchers.Default) {
            if (canGoBack()) {
                savedStateHandle[USE_BACK_ANIM] = useBackAnimation
                savedStateHandle[CURRENT_SCREEN] = backStack.value.last()
                backStack.value.removeLast()
            }
        }

    fun canGoBack(): Boolean {
        return backStack.value.isNotEmpty()
    }

    fun getCurrentScreen(): StateFlow<Screen> {
        return currentScreen
    }

    /**
     * Animated Screen. Using in [NavigationHost]. Be sure to not use the same [screen] parameter twice, it'll override the highest one in your code.
     *
     * @param screen The [Screen] that represents [content].
     * @param enterAnim Enter Animation.
     * @param backEnterAnim Enter Animation for navigating back.
     * @param exitAnim Exit Animation.
     * @param backExitAnim Exit Animation for navigating back.
     * @param content The Screen content to show when [Navigator.currentScreen] equals [screen].
     */
    @SuppressLint("ComposableNaming")
    @Composable
    fun composable(
        screen: Screen,
        enterAnim: EnterTransition = Transitions.FadeTransitionIn,
        backEnterAnim: EnterTransition = Transitions.BackSlidingTransitionIn,
        exitAnim: ExitTransition = Transitions.FadeTransitionOut,
        backExitAnim: ExitTransition = Transitions.BackSlidingTransitionOut,
        content: @Composable () -> Unit
    ) {
        AnimatedVisibility(
            visible = getCurrentScreen().collectAsState().value == screen,
            enter = if (!useBackAnimation.collectAsState().value) enterAnim else backEnterAnim,
            exit = if (!useBackAnimation.collectAsState().value) exitAnim else backExitAnim,
            label = ""
        ) {
            content()
        }
    }

    @AssistedFactory
    interface Factory {
        fun create(startScreen: Screen): Navigator
    }
}

/**
 * Navigation Host. Contains [Navigator.composable]s in [content].
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
        modifier = Modifier
            .fillMaxSize()
            .background(colorBetweenAnimations)
    )

    content(navigator)
}