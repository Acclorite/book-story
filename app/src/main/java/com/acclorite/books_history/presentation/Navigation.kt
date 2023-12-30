package com.acclorite.books_history.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


private const val CURRENT_SCREEN = "current_screen"
private const val BACKSTACK = "back_stack"

/**
 * All screens are listed here, later each screen will be passed as a param for [Navigator.composable] function.
 */
enum class Screen {
    LIBRARY,
    HISTORY,
    BROWSE,


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

    fun init(startScreen: Screen) {
        savedStateHandle[CURRENT_SCREEN] = startScreen
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
        enterAnim: EnterTransition = fadeIn(tween(300)),
        exitAnim: ExitTransition = fadeOut(tween(300)),
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
    activity: ComponentActivity,
    colorBetweenAnimations: Color = MaterialTheme.colorScheme.surface,
    content: @Composable Navigator.() -> Unit
) {
    activity.apply {
        val navigator by viewModels<Navigator>(
            extrasProducer = {
                defaultViewModelCreationExtras.withCreationCallback<Navigator.Factory> { factory ->
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
}