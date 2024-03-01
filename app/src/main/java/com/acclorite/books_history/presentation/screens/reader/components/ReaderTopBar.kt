package com.acclorite.books_history.presentation.screens.reader.components

import androidx.activity.ComponentActivity
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acclorite.books_history.R
import com.acclorite.books_history.presentation.Argument
import com.acclorite.books_history.presentation.Navigator
import com.acclorite.books_history.presentation.Screen
import com.acclorite.books_history.presentation.data.removeDigits
import com.acclorite.books_history.presentation.data.removeTrailingZero
import com.acclorite.books_history.presentation.screens.reader.data.ReaderEvent
import com.acclorite.books_history.presentation.screens.reader.data.ReaderViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReaderTopBar(viewModel: ReaderViewModel, navigator: Navigator, containerColor: Color) {
    val context = LocalContext.current as ComponentActivity
    val state by viewModel.state.collectAsState()
    val book = state.book

    TopAppBar(
        navigationIcon = {
            IconButton(onClick = {
                navigator.navigateBack()
                viewModel.onEvent(ReaderEvent.OnShowSystemBars(context))
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Go back",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        title = {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    book.title,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = {
                                navigator.navigateWithoutBackStack(
                                    Screen.BOOK_INFO,
                                    Argument(
                                        "book",
                                        book
                                    )
                                )
                            }
                        )
                        .basicMarquee(
                            delayMillis = 4000
                        )
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    stringResource(
                        id = R.string.read_query,
                        (book.progress * 100)
                            .toDouble()
                            .removeDigits(2)
                            .removeTrailingZero()
                            .dropWhile { it == '-' } + "%"
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        },
        actions = {
            IconButton(onClick = {
                viewModel.onEvent(ReaderEvent.OnShowHideSettingsBottomSheet)
            }) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Open Settings",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = containerColor
        )
    )
}