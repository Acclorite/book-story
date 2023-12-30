package com.acclorite.books_history.domain.use_case

import androidx.activity.ComponentActivity
import androidx.annotation.Size

interface ChangeLanguage {
    fun execute(
        @Size(max = 2) language: String,
        activity: ComponentActivity
    )
}