package ua.acclorite.book_story.presentation.license_info

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.entity.Library
import ua.acclorite.book_story.presentation.core.components.common.LazyColumnWithScrollbar

@Composable
fun LicenseInfoLayout(
    library: Library,
    paddingValues: PaddingValues,
    listState: LazyListState
) {
    LazyColumnWithScrollbar(
        Modifier
            .fillMaxSize()
            .padding(top = paddingValues.calculateTopPadding()),
        state = listState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            library.licenses.toList(),
            key = { it.name }
        ) { license ->
            LicenseInfoItem(license = license)
        }
    }
}