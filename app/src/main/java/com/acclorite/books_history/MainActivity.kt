package com.acclorite.books_history

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.acclorite.books_history.ui.theme.BooksHistoryResurrectionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BooksHistoryResurrectionTheme {

            }
        }
    }
}