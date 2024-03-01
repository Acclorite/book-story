package com.acclorite.books_history.presentation.screens.settings.nested.general

import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.acclorite.books_history.R
import com.acclorite.books_history.domain.model.ChipItem
import com.acclorite.books_history.presentation.Navigator
import com.acclorite.books_history.presentation.data.MainEvent
import com.acclorite.books_history.presentation.data.MainViewModel
import com.acclorite.books_history.presentation.screens.settings.components.ChipsWithTitle
import com.acclorite.books_history.ui.elevation
import com.acclorite.books_history.util.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GeneralSettings(
    mainViewModel: MainViewModel,
    navigator: Navigator
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalContext.current as ComponentActivity

    val language = mainViewModel.language.collectAsState().value!!

    Scaffold(
        Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .windowInsetsPadding(WindowInsets.navigationBars),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        stringResource(id = R.string.general_settings),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateBack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Go back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.elevation()
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(top = paddingValues.calculateTopPadding())
        ) {
            item {
                ChipsWithTitle(
                    title = stringResource(id = R.string.language_option),
                    chips = Constants.LANGUAGES.map {
                        ChipItem(
                            it.first,
                            it.second,
                            MaterialTheme.typography.labelLarge,
                            it.first == language
                        )
                    }
                ) {
                    mainViewModel.onEvent(
                        MainEvent.OnChangeLanguage(
                            it.id,
                            context
                        )
                    )
                }
            }
        }
    }
}