package com.acclorite.books_history.presentation.components

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.acclorite.books_history.ui.DefaultTransition

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnimatedTopAppBar(
    containerColor: Color = MaterialTheme.colorScheme.surface,
    scrolledContainerColor: Color?,

    scrollBehavior: TopAppBarScrollBehavior?,
    isTopBarScrolled: Boolean?,

    content1Visibility: Boolean,
    content1NavigationIcon: @Composable () -> Unit,
    content1Title: @Composable () -> Unit,
    content1Actions: @Composable RowScope.() -> Unit,

    content2Visibility: Boolean,
    content2NavigationIcon: @Composable () -> Unit,
    content2Title: @Composable () -> Unit,
    content2Actions: @Composable RowScope.() -> Unit,

    content3Visibility: Boolean,
    content3NavigationIcon: @Composable () -> Unit,
    content3Title: @Composable () -> Unit,
    content3Actions: @Composable RowScope.() -> Unit
) {
    val color =
        if (isTopBarScrolled == true && scrolledContainerColor != null) scrolledContainerColor
        else containerColor

    val animatedColor by animateColorAsState(
        targetValue = color,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "TopAppBar color animation"
    )

    if (scrollBehavior != null && scrolledContainerColor != null) {
        val isScrolled = scrollBehavior.state.overlappedFraction > 0.01f
        val statusBarPadding = with(LocalDensity.current) {
            WindowInsets.statusBars.getTop(LocalDensity.current).toDp()
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp + statusBarPadding)
                .background(
                    if (isScrolled) scrolledContainerColor
                    else containerColor
                )
        )
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        DefaultTransition(visible = content1Visibility) {
            TopAppBar(
                navigationIcon = content1NavigationIcon,
                title = content1Title,
                actions = content1Actions,
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = animatedColor,
                    scrolledContainerColor = scrolledContainerColor ?: Color.Transparent
                )
            )
        }

        DefaultTransition(visible = content2Visibility) {
            TopAppBar(
                navigationIcon = content2NavigationIcon,
                title = content2Title,
                actions = content2Actions,
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = animatedColor,
                    scrolledContainerColor = scrolledContainerColor ?: Color.Transparent
                )
            )
        }

        DefaultTransition(visible = content3Visibility) {
            TopAppBar(
                navigationIcon = content3NavigationIcon,
                title = content3Title,
                actions = content3Actions,
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = animatedColor,
                    scrolledContainerColor = scrolledContainerColor ?: Color.Transparent
                )
            )
        }
    }
}











