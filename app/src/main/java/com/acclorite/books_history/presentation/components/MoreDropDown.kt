package com.acclorite.books_history.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.acclorite.books_history.R
import com.acclorite.books_history.presentation.Navigator
import com.acclorite.books_history.presentation.Screen

@Composable
fun MoreDropDown(navigator: Navigator) {
    var showDropDown by remember { mutableStateOf(false) }

    val startPadding = 12.dp
    val endPadding = 36.dp

    Box {
        IconButton(onClick = {
            showDropDown = true
        }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "Show drop down",
                modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        DropdownMenu(
            expanded = showDropDown,
            onDismissRequest = { showDropDown = false },
            offset = DpOffset(10.dp, 0.dp)
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
                    navigator.navigate(Screen.SETTINGS)
                    showDropDown = false
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
                    navigator.navigate(Screen.HELP)
                    showDropDown = false
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
                    navigator.navigate(Screen.ABOUT)
                    showDropDown = false
                },
                contentPadding = PaddingValues(start = startPadding, end = endPadding)
            )


        }
    }
}