package ua.acclorite.book_story.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.data.LocalNavigator
import ua.acclorite.book_story.presentation.data.Screen

/**
 * More drop down.
 * Navigates to Settings, About and Help screens.
 */
@Composable
fun MoreDropDown() {
    val navigator = LocalNavigator.current
    var showDropDown by remember { mutableStateOf(false) }

    val startPadding = remember { 12.dp }
    val endPadding = remember { 36.dp }

    Box {
        CustomIconButton(
            icon = Icons.Default.MoreVert,
            contentDescription = R.string.show_dropdown_content_desc,
            disableOnClick = false,
            enabled = !showDropDown
        ) {
            showDropDown = true
        }

        DropdownMenu(
            expanded = showDropDown,
            onDismissRequest = { showDropDown = false },
            offset = DpOffset((-15).dp, 0.dp)
        ) {
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.settings_screen),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                onClick = {
                    navigator.navigate(Screen.Settings)
                },
                contentPadding = PaddingValues(start = startPadding, end = endPadding)
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.help_screen),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                onClick = {
                    navigator.navigate(Screen.Help(false))
                },
                contentPadding = PaddingValues(start = startPadding, end = endPadding)
            )
            DropdownMenuItem(
                text = {
                    Text(
                        text = stringResource(id = R.string.about_screen),
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.bodyLarge
                    )
                },
                onClick = {
                    navigator.navigate(Screen.About)
                },
                contentPadding = PaddingValues(start = startPadding, end = endPadding)
            )


        }
    }
}