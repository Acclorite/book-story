@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.browse.sort.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.browse.BrowseSortOrder
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

fun LazyListScope.BrowseSortOption() {
    items(BrowseSortOrder.entries, key = { it.name }) {
        val mainModel = hiltViewModel<MainModel>()
        val state = mainModel.state.collectAsStateWithLifecycle()

        BrowseSortOptionItem(
            item = it,
            isSelected = state.value.browseSortOrder == it,
            isDescending = state.value.browseSortOrderDescending
        ) {
            if (state.value.browseSortOrder == it) {
                mainModel.onEvent(
                    MainEvent.OnChangeBrowseSortOrderDescending(
                        !state.value.browseSortOrderDescending
                    )
                )
            } else {
                mainModel.onEvent(MainEvent.OnChangeBrowseSortOrderDescending(true))
                mainModel.onEvent(MainEvent.OnChangeBrowseSortOrder(it.name))
            }
        }
    }
}

@Composable
private fun BrowseSortOptionItem(
    item: BrowseSortOrder,
    isSelected: Boolean,
    isDescending: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 24.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (isDescending) Icons.Default.ArrowDownward
            else Icons.Default.ArrowUpward,
            contentDescription = stringResource(id = R.string.sort_order_content_desc),
            modifier = Modifier
                .size(28.dp),
            tint = if (isSelected) MaterialTheme.colorScheme.primary
            else Color.Transparent
        )
        Spacer(modifier = Modifier.width(24.dp))

        Text(
            text = stringResource(
                when (item) {
                    BrowseSortOrder.NAME -> R.string.browse_sort_order_name
                    BrowseSortOrder.FILE_FORMAT -> R.string.browse_sort_order_file_format
                    BrowseSortOrder.FILE_TYPE -> R.string.browse_sort_order_file_type
                    BrowseSortOrder.LAST_MODIFIED -> R.string.browse_sort_order_last_modified
                    BrowseSortOrder.FILE_SIZE -> R.string.browse_sort_order_file_size
                }
            ),
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}