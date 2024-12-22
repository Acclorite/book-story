package ua.acclorite.book_story.presentation.licenses

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.entity.Library
import ua.acclorite.book_story.presentation.core.components.common.AnimatedVisibility
import ua.acclorite.book_story.presentation.core.components.common.LazyColumnWithScrollbar

@Composable
fun LicensesLayout(
    licenses: List<Library>,
    paddingValues: PaddingValues,
    listState: LazyListState,
    navigateToLicenseInfo: (Library) -> Unit
) {
    AnimatedVisibility(
        visible = licenses.isNotEmpty(),
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        LazyColumnWithScrollbar(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding()),
            state = listState,
            contentPadding = PaddingValues(vertical = 12.dp)
        ) {
            items(
                licenses,
                key = { it.uniqueId }
            ) { library ->
                LicensesItem(library = library) {
                    navigateToLicenseInfo(library)
                }
            }
        }
    }
}