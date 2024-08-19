@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.screens.settings.nested.browse.components.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.customItems
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.screens.settings.nested.browse.data.BrowseSortOrder

/**
 * Browse Sort Order setting.
 * Lets user choose how to sort files in Browse.
 */
fun LazyListScope.BrowseSortOrderSetting(
    state: State<MainState>,
    onMainEvent: (MainEvent) -> Unit
) {
    customItems(BrowseSortOrder.entries, key = { it.name }) {
        SortItem(
            item = it,
            isSelected = state.value.browseSortOrder!! == it,
            isDescending = state.value.browseSortOrderDescending!!
        ) {
            if (state.value.browseSortOrder!! == it) {
                onMainEvent(
                    MainEvent.OnChangeBrowseSortOrderDescending(
                        !state.value.browseSortOrderDescending!!
                    )
                )
            } else {
                onMainEvent(MainEvent.OnChangeBrowseSortOrderDescending(true))
                onMainEvent(MainEvent.OnChangeBrowseSortOrder(it.name))
            }
        }
    }
}

/**
 * Sort Item.
 * Can sort items by descending or ascending.
 *
 * @param item [BrowseSortOrder].
 * @param isSelected Whether items are currently sorted by this item.
 * @param isDescending Whether sorted by descending.
 * @param onClick OnClick callback.
 */
@Composable
private fun LazyItemScope.SortItem(
    item: BrowseSortOrder,
    isSelected: Boolean,
    isDescending: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .animateItem()
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