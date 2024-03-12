package ua.acclorite.book_story.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.data.Navigator

/**
 * Go back arrow button. Prevents double or triple clicking on it while going back action is performed.
 */
@Composable
fun GoBackButton(navigator: Navigator, customOnClick: () -> Unit = {}) {
    var isClicked by remember { mutableStateOf(false) }

    IconButton(
        enabled = !isClicked,
        onClick = {
            isClicked = true
            navigator.navigateBack()
            customOnClick()
        }
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = stringResource(id = R.string.go_back_content_desc),
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}