package com.acclorite.books_history

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.datastore.core.DataStore
import androidx.lifecycle.viewmodel.compose.viewModel
import com.acclorite.books_history.data.use_case.ChangeLanguageImpl
import com.acclorite.books_history.presentation.MainViewModel
import com.acclorite.books_history.presentation.NavigationHost
import com.acclorite.books_history.presentation.Screen
import com.acclorite.books_history.ui.theme.BooksHistoryResurrectionTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BooksHistoryResurrectionTheme {
                NavigationHost(startScreen = Screen.LIBRARY, this) {

                }
            }
        }
    }
}