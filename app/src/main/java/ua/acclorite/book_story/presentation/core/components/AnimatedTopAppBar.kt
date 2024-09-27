package ua.acclorite.book_story.presentation.core.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp

data class AnimatedTopAppBarData(
    val contentVisibility: Boolean? = null,
    val contentNavigationIcon: @Composable () -> Unit,
    val contentTitle: @Composable () -> Unit,
    val contentActions: @Composable RowScope.() -> Unit
)

/**
 * Animated top app bar.
 * Has up to 3 different top bars inside it. Follow [TopAppBar] for more info.
 *
 * @param containerColor Container color of the TopBar.
 * @param scrolledContainerColor Scrolled container color of the TopBar.
 * @param scrollBehavior [TopAppBarScrollBehavior].
 * @param isTopBarScrolled Whether isScrolled state should be forced or not.
 * @param animatedTopBars Pass a list of all [AnimatedTopAppBarData] to show.
 * @param customContent Custom content below [TopAppBar].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedTopAppBar(
    containerColor: Color = MaterialTheme.colorScheme.surface,
    scrolledContainerColor: Color = MaterialTheme.colorScheme.surfaceContainer,

    scrollBehavior: TopAppBarScrollBehavior?,
    isTopBarScrolled: Boolean?,

    animatedTopBars: List<AnimatedTopAppBarData>,
    customContent: @Composable ColumnScope.() -> Unit = {}
) {
    val animatedContainerColor = lerp(
        containerColor,
        scrolledContainerColor,
        animateFloatAsState(
            if (isTopBarScrolled == true) 1f else 0f,
            animationSpec = spring(
                stiffness = Spring.StiffnessMediumLow
            )
        ).value
    )

    Column(
        Modifier
            .fillMaxWidth()
            .background(animatedContainerColor)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            animatedTopBars.forEach { data ->
                CustomAnimatedVisibility(
                    visible = data.contentVisibility ?: false,
                    enter = fadeIn(spring(stiffness = Spring.StiffnessMediumLow)),
                    exit = fadeOut(spring(stiffness = Spring.StiffnessMediumLow))
                ) {
                    TopAppBar(
                        windowInsets = WindowInsets.statusBars,
                        navigationIcon = data.contentNavigationIcon,
                        title = data.contentTitle,
                        actions = data.contentActions,
                        scrollBehavior = scrollBehavior,
                        modifier = Modifier.fillMaxWidth(),
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = if (isTopBarScrolled != null) Color.Transparent
                            else containerColor,
                            scrolledContainerColor = scrolledContainerColor
                        )
                    )
                }
            }
        }
        customContent()
    }
}







