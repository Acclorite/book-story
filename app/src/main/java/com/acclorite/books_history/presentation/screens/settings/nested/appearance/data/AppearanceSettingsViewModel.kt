package com.acclorite.books_history.presentation.screens.settings.nested.appearance.data

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalFoundationApi::class)
@HiltViewModel
class AppearanceSettingsViewModel @Inject constructor(

) : ViewModel() {

    private val _state = MutableStateFlow(AppearanceSettingsState())
    val state = _state.asStateFlow()

    fun onEvent(event: AppearanceSettingsEvent) {
        when (event) {
            is AppearanceSettingsEvent.OnShowHideDarkThemeDialog -> {
                _state.update {
                    it.copy(
                        showDarkThemeDialog = !it.showDarkThemeDialog
                    )
                }
            }
        }
    }
}