package ua.acclorite.book_story.presentation.components

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

/**
 * Animated top app bar.
 * Has up to 3 different top bars inside it. Follow [TopAppBar] for more info.
 *
 * @param containerColor Container color of the TopBar.
 * @param scrolledContainerColor Scrolled container color of the TopBar.
 * @param scrollBehavior [TopAppBarScrollBehavior].
 * @param isTopBarScrolled Whether isScrolled state should be forced or not.
 * @param content1Visibility Whether first top app bar should be shown.
 * @param content2Visibility Whether second top app bar should be shown.
 * @param content3Visibility Whether third top app bar should be shown.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedTopAppBar(
    containerColor: Color = MaterialTheme.colorScheme.surface,
    scrolledContainerColor: Color = MaterialTheme.colorScheme.surfaceContainer,

    scrollBehavior: TopAppBarScrollBehavior?,
    isTopBarScrolled: Boolean?,

    content1Visibility: Boolean? = null,
    content1NavigationIcon: @Composable () -> Unit,
    content1Title: @Composable () -> Unit,
    content1Actions: @Composable RowScope.() -> Unit,

    content2Visibility: Boolean? = null,
    content2NavigationIcon: @Composable () -> Unit = {},
    content2Title: @Composable () -> Unit = {},
    content2Actions: @Composable RowScope.() -> Unit = {},

    content3Visibility: Boolean? = null,
    content3NavigationIcon: @Composable () -> Unit = {},
    content3Title: @Composable () -> Unit = {},
    content3Actions: @Composable RowScope.() -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        val density = LocalDensity.current
        val statusBarPadding = with(density) {
            WindowInsets.statusBars.getTop(density).toDp()
        }

        if (containerColor.alpha != 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp + statusBarPadding)
                    .background(
                        if (isTopBarScrolled == true) scrolledContainerColor
                        else containerColor
                    )
            )
        }

        val animatedContainerColor = remember(
            containerColor,
            scrolledContainerColor,
            isTopBarScrolled
        ) {
            lerp(
                containerColor,
                scrolledContainerColor,
                FastOutLinearInEasing.transform(if (isTopBarScrolled == true) 1f else 0f)
            )
        }

        CustomAnimatedVisibility(
            visible = content1Visibility ?: true,
            enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)),
            exit = fadeOut(spring(stiffness = Spring.StiffnessMediumLow))
        ) {
            TopAppBar(
                windowInsets = WindowInsets.statusBars,
                navigationIcon = content1NavigationIcon,
                title = content1Title,
                actions = content1Actions,
                scrollBehavior = scrollBehavior,
                modifier = Modifier.fillMaxWidth(),
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = animatedContainerColor,
                    scrolledContainerColor = scrolledContainerColor
                )
            )
        }

        if (content2Visibility != null) {
            CustomAnimatedVisibility(
                visible = content2Visibility,
                enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)),
                exit = fadeOut(spring(stiffness = Spring.StiffnessMediumLow))
            ) {
                TopAppBar(
                    windowInsets = WindowInsets.statusBars,
                    navigationIcon = content2NavigationIcon,
                    title = content2Title,
                    actions = content2Actions,
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = animatedContainerColor,
                        scrolledContainerColor = scrolledContainerColor
                    )
                )
            }
        }

        if (content3Visibility != null) {
            CustomAnimatedVisibility(
                visible = content3Visibility,
                enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)),
                exit = fadeOut(spring(stiffness = Spring.StiffnessMediumLow))
            ) {
                TopAppBar(
                    windowInsets = WindowInsets.statusBars,
                    navigationIcon = content3NavigationIcon,
                    title = content3Title,
                    actions = content3Actions,
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = animatedContainerColor,
                        scrolledContainerColor = scrolledContainerColor
                    )
                )
            }
        }
    }
}







