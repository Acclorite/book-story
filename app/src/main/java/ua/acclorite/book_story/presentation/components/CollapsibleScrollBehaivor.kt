package ua.acclorite.book_story.presentation.components

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.flow.collectLatest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarDefaults.collapsibleScrollBehaviorWithLazyListState(

): Pair<TopAppBarScrollBehavior, LazyListState> {
    val listState = rememberLazyListState()

    var canScroll by remember { mutableStateOf(false) }
    val scrollBehavior = exitUntilCollapsedScrollBehavior(
        canScroll = {
            listState.canScrollForward || canScroll
        }
    )

    LaunchedEffect(scrollBehavior.state) {
        snapshotFlow {
            scrollBehavior.state.collapsedFraction
        }.collectLatest { fraction ->
            canScroll = fraction > 0.01f
        }
    }

    return scrollBehavior to listState
}















