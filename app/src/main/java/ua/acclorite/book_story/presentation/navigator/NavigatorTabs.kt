package ua.acclorite.book_story.presentation.navigator

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ContentTransform
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.receiveAsFlow
import ua.acclorite.book_story.domain.navigator.Screen
import ua.acclorite.book_story.presentation.core.util.LocalActivity

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun NavigatorTabs(
    currentTab: Screen,
    transitionSpec: AnimatedContentTransitionScope<Screen>.() -> ContentTransform,
    navigationBar: @Composable () -> Unit,
    navigationRail: @Composable () -> Unit,
    content: @Composable (Screen) -> Unit
) {
    val windowClass = calculateWindowSizeClass(activity = LocalActivity.current)
    val tabletUI = remember(windowClass) {
        windowClass.widthSizeClass != WindowWidthSizeClass.Compact
    }
    val layoutDirection = LocalLayoutDirection.current
    val showNavigatorBottomSheet = navigatorBottomSheetChannel
        .receiveAsFlow()
        .collectAsStateWithLifecycle(false)

    if (showNavigatorBottomSheet.value) {
        NavigatorBottomSheet()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (!tabletUI) navigationBar()
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (tabletUI) navigationRail()

            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(
                        end = it.calculateEndPadding(layoutDirection),
                        bottom = it.calculateBottomPadding(),
                        start = it.calculateStartPadding(layoutDirection)
                    )
            ) {
                AnimatedContent(
                    targetState = currentTab,
                    transitionSpec = transitionSpec,
                    content = {
                        content(it)
                    }
                )
            }
        }
    }
}