package ua.acclorite.book_story.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.ui.DefaultTransition
import ua.acclorite.book_story.ui.elevation

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedTopAppBar(
    containerColor: Color = MaterialTheme.colorScheme.surface,
    scrolledContainerColor: Color? = MaterialTheme.elevation(),

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
        if ((scrollBehavior != null || isTopBarScrolled != null) && scrolledContainerColor != null) {

            val fraction by remember {
                derivedStateOf {
                    scrollBehavior?.state?.overlappedFraction ?: 0f
                }
            }

            val isScrolled = if (scrollBehavior != null) {
                fraction > 0.01f
            } else isTopBarScrolled ?: false

            val color = if (isScrolled) scrolledContainerColor else containerColor
            val animatedColor by animateColorAsState(
                targetValue = color,
                animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
                label = "TopAppBar color animation"
            )

            val statusBarPadding = with(LocalDensity.current) {
                WindowInsets.statusBars.getTop(LocalDensity.current).toDp()
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp + statusBarPadding)
                    .background(
                        animatedColor
                    )
            )
        }

        DefaultTransition(visible = content1Visibility ?: true) {
            TopAppBar(
                navigationIcon = content1NavigationIcon,
                title = content1Title,
                actions = content1Actions,
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent
                )
            )
        }

        if (content2Visibility != null) {
            DefaultTransition(visible = content2Visibility) {
                TopAppBar(
                    navigationIcon = content2NavigationIcon,
                    title = content2Title,
                    actions = content2Actions,
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent
                    )
                )
            }
        }

        if (content3Visibility != null) {
            DefaultTransition(visible = content3Visibility) {
                TopAppBar(
                    navigationIcon = content3NavigationIcon,
                    title = content3Title,
                    actions = content3Actions,
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        scrolledContainerColor = Color.Transparent
                    )
                )
            }
        }
    }
}











