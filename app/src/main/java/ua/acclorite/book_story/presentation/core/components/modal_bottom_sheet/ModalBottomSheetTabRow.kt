package ua.acclorite.book_story.presentation.core.components.modal_bottom_sheet

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

/**
 * Primary Tab Row. Used in BottomSheet in pair with HorizontalPager.
 * Better to not use too many tabs, as they will overflow.
 *
 * @param selectedTabIndex Selected index, typically pager.currentPage.
 * @param tabs Tabs to be shown, should match HorizontalPager's page count.
 * @param onClick OnClick callback, gives you an index of clicked item.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomSheetTabRow(
    selectedTabIndex: Int,
    tabs: List<String>,
    onClick: (index: Int) -> Unit
) {
    PrimaryTabRow(
        modifier = Modifier
            .fillMaxWidth(),
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.Transparent
    ) {
        tabs.forEachIndexed { index, tabItem ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = {
                    onClick(index)
                },
                selectedContentColor = MaterialTheme.colorScheme.primary,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                text = {
                    Text(
                        tabItem,
                        style = MaterialTheme.typography.bodyLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}