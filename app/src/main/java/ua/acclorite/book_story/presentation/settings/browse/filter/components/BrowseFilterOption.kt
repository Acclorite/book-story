@file:Suppress("FunctionName")

package ua.acclorite.book_story.presentation.settings.browse.filter.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ua.acclorite.book_story.presentation.core.constants.Constants
import ua.acclorite.book_story.presentation.core.constants.provideExtensions
import ua.acclorite.book_story.ui.main.MainEvent
import ua.acclorite.book_story.ui.main.MainModel

fun LazyListScope.BrowseFilterOption() {
    items(Constants.provideExtensions(), key = { it }) {
        val mainModel = hiltViewModel<MainModel>()
        val state = mainModel.state.collectAsStateWithLifecycle()

        BrowseFilterOptionItem(
            item = it,
            isSelected = state.value.browseIncludedFilterItems.any { item ->
                item == it
            }
        ) {
            mainModel.onEvent(
                MainEvent.OnChangeBrowseIncludedFilterItem(it)
            )
        }
    }
}

@Composable
private fun BrowseFilterOptionItem(
    item: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(horizontal = 14.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = isSelected,
            onCheckedChange = { onClick() }
        )
        Spacer(modifier = Modifier.width(14.dp))

        Text(
            text = item,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}