package ua.acclorite.book_story.presentation.core.components.common

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import ua.acclorite.book_story.R

/**
 * Search Text Field.
 * Used in main screens for searching.
 *
 * @param modifier Modifier to apply.
 * @param query Search query.
 * @param onQueryChange Callback to change [query].
 * @param onSearch Search action (refresh list, fetch filtered books etc..).
 */
@Composable
fun SearchTextField(
    modifier: Modifier = Modifier,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    val keyboardManager = LocalSoftwareKeyboardController.current

    BasicTextField(
        value = query,
        singleLine = true,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = MaterialTheme.typography.titleLarge.fontSize,
            lineHeight = MaterialTheme.typography.titleLarge.lineHeight,
            fontFamily = MaterialTheme.typography.titleLarge.fontFamily
        ),
        modifier = modifier,
        onValueChange = onQueryChange,
        keyboardOptions = KeyboardOptions(
            KeyboardCapitalization.Words,
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
                keyboardManager?.hide()
            }
        ),
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant)
    ) { innerText ->
        Box(contentAlignment = Alignment.CenterStart) {
            if (query.isEmpty()) {
                Text(
                    text = stringResource(id = R.string.search_field_empty),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            innerText()
        }
    }
}