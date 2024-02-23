package com.acclorite.books_history.presentation

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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acclorite.books_history.ui.Transitions
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


private const val CURRENT_SCREEN = "current_screen"
private const val BACKSTACK = "back_stack"

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

    ABOUT,
    HELP
}

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
    private val backStack = savedStateHandle.getStateFlow(BACKSTACK, mutableListOf<Screen>())
    private val arguments = mutableStateListOf<Argument>()

    private fun putArgument(argument: Argument) {
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

    fun navigate(screen: Screen, vararg args: Argument) =
        viewModelScope.launch(Dispatchers.Default) {
            backStack.value.add(currentScreen.value)

            args.forEach {
                putArgument(it)
            }

            savedStateHandle[CURRENT_SCREEN] = screen
        }

    fun navigateBack() = viewModelScope.launch(Dispatchers.Default) {
        if (canGoBack()) {
            savedStateHandle[CURRENT_SCREEN] = backStack.value.last()
            backStack.value.removeLast()
        }
    }

    fun canGoBack(): Boolean {
        return backStack.value.isNotEmpty()
    }

    fun clearBackStack() {
        backStack.value.clear()
    }

    fun getCurrentScreen(): StateFlow<Screen> {
        return currentScreen
    }

    /**
     * Animated Screen. Using in [NavigationHost]. Be sure to not use the same [screen] parameter twice, it'll override the highest one in your code.
     *
     * @param screen The [Screen] that represents [content].
     * @param enterAnim Enter Animation.
     * @param exitAnim Exit Animation.
     * @param content The Screen content to show when [Navigator.currentScreen] equals [screen].
     */
    @SuppressLint("ComposableNaming")
    @Composable
    fun composable(
        screen: Screen,
        enterAnim: EnterTransition = Transitions.DefaultTransitionIn,
        exitAnim: ExitTransition = Transitions.DefaultTransitionOut,
        content: @Composable () -> Unit
    ) {
        AnimatedVisibility(
            visible = getCurrentScreen().collectAsState().value == screen,
            enter = enterAnim,
            exit = exitAnim,
            label = screen.toString()
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