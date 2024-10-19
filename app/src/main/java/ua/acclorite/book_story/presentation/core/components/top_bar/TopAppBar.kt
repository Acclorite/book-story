package ua.acclorite.book_story.presentation.core.components.top_bar

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
import ua.acclorite.book_story.domain.util.ID
import ua.acclorite.book_story.presentation.core.components.common.AnimatedVisibility

data class TopAppBarData(
    val contentID: ID,
    val contentNavigationIcon: @Composable () -> Unit,
    val contentTitle: @Composable () -> Unit,
    val contentActions: @Composable RowScope.() -> Unit
)

/**
 * Top app bar.
 * Has up to 3 different top bars inside it. Follow [TopAppBar] for more info.
 *
 * @param containerColor Container color of the TopBar.
 * @param scrolledContainerColor Scrolled container color of the TopBar.
 * @param scrollBehavior [TopAppBarScrollBehavior].
 * @param isTopBarScrolled Whether isScrolled state should be forced or not.
 * @param shownTopBar [ID] of the top bar to show.
 * @param topBars Pass a list of all [TopAppBarData] to show.
 * @param customContent Custom content below [TopAppBar].
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    containerColor: Color = MaterialTheme.colorScheme.surface,
    scrolledContainerColor: Color = MaterialTheme.colorScheme.surfaceContainer,

    scrollBehavior: TopAppBarScrollBehavior?,
    isTopBarScrolled: Boolean?,

    shownTopBar: ID,
    topBars: List<TopAppBarData>,
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
            topBars.forEach { data ->
                AnimatedVisibility(
                    visible = data.contentID == shownTopBar,
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