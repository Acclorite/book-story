package ua.acclorite.book_story.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.data.Navigator

/**
 * Go back arrow button. Prevents double or triple clicking on it while going back action is performed.
 */
@Composable
fun GoBackButton(navigator: Navigator, enabled: Boolean = true, customOnClick: () -> Unit = {}) {
    CustomIconButton(
        icon = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = R.string.go_back_content_desc,
        disableOnClick = true,
        enabled = enabled
    ) {
        customOnClick()
        navigator.navigateBack()
    }
}