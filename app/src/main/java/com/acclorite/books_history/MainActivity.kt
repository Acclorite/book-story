package com.acclorite.books_history

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.acclorite.books_history.presentation.MainViewModel
import com.acclorite.books_history.presentation.NavigationHost
import com.acclorite.books_history.presentation.Screen
import com.acclorite.books_history.ui.theme.BooksHistoryResurrectionTheme
import com.acclorite.books_history.ui.theme.isDark
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mainViewModel.init(this)
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                !mainViewModel.isReady.value
            }
        }

        setContent {
            val theme = mainViewModel.theme.collectAsState().value!!
            val darkTheme = mainViewModel.darkTheme.collectAsState().value!!

            BooksHistoryResurrectionTheme(
                theme = theme,
                isDark = darkTheme.isDark()
            ) {
                NavigationHost(startScreen = Screen.LIBRARY, this) {

                }
            }
        }
    }
}