package ua.acclorite.book_story.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.util.OnNavigate

/**
 * Go back arrow button.
 * Prevents double or triple clicking on it while going back action is performed.
 *
 * @param onNavigate [OnNavigate].
 * @param enabled Whether this button is enabled.
 * @param customOnClick Call before going back.
 */
@Composable
fun GoBackButton(onNavigate: OnNavigate, enabled: Boolean = true, customOnClick: () -> Unit = {}) {
    CustomIconButton(
        icon = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = R.string.go_back_content_desc,
        disableOnClick = true,
        enabled = enabled
    ) {
        customOnClick()
        onNavigate {
            navigateBack()
        }
    }
}