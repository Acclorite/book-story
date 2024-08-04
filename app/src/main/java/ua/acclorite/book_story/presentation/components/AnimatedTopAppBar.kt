package ua.acclorite.book_story.presentation.components

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
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R

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
 * @param customContent Custom content below [TopAppBar].
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
    content3Actions: @Composable RowScope.() -> Unit = {},

    customContent: @Composable ColumnScope.() -> Unit = {}
) {
    val animatedContainerColor = lerp(
        containerColor,
        scrolledContainerColor,
        animateFloatAsState(
            if (isTopBarScrolled == true) 1f else 0f,
            animationSpec = spring(
                stiffness = Spring.StiffnessMediumLow
            ),
            label = stringResource(id = R.string.top_app_bar_anim_content_desc)
        ).value
    )

    Column(
        Modifier
            .fillMaxWidth()
            .background(animatedContainerColor)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
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
                        containerColor = if (isTopBarScrolled != null) Color.Transparent
                        else containerColor,
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
                            containerColor = if (isTopBarScrolled != null) Color.Transparent
                            else containerColor,
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







