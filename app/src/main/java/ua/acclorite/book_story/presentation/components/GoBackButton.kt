package ua.acclorite.book_story.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.data.Navigator

/**
 * Go back arrow button. Prevents double or triple clicking on it while going back action is performed.
 */
@Composable
fun GoBackButton(navigator: Navigator, customOnClick: () -> Unit = {}) {
    CustomIconButton(
        icon = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = stringResource(id = R.string.go_back_content_desc),
        disableOnClick = true
    ) {
        navigator.navigateBack()
        customOnClick()
    }
}