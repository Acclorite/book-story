package com.acclorite.books_history.presentation.screens.settings.nested.general.data

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@OptIn(ExperimentalFoundationApi::class)
@HiltViewModel
class GeneralSettingsViewModel @Inject constructor(

) : ViewModel() {

    private val _state = MutableStateFlow(GeneralSettingsState())
    val state = _state.asStateFlow()

    fun onEvent(event: GeneralSettingsEvent) {
        when (event) {
            is GeneralSettingsEvent.OnShowHideLanguageDialog -> {
                _state.update {
                    it.copy(
                        showLanguageDialog = !it.showLanguageDialog
                    )
                }
            }
        }
    }

}











