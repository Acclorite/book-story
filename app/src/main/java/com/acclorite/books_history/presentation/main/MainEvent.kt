package com.acclorite.books_history.presentation.main

import androidx.activity.ComponentActivity

sealed class MainEvent() {
    data class OnChangeLanguage(val lang: String, val activity: ComponentActivity) : MainEvent()
    data class OnLocaleUpdate(val activity: ComponentActivity) : MainEvent()
    data class OnChangeTheme(val theme: String) : MainEvent()
    data class OnChangeDarkTheme(val darkTheme: String) : MainEvent()

}