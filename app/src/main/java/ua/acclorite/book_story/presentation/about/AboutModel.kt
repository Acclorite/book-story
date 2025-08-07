/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.about

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AboutModel @Inject constructor() : ViewModel() {

    private val _effects = MutableSharedFlow<AboutEffect>()
    val effects = _effects.asSharedFlow()

    fun onEvent(event: AboutEvent) {
        viewModelScope.launch {
            when (event) {
                is AboutEvent.OnNavigateToBrowserPage -> {
                    _effects.emit(AboutEffect.OnNavigateToBrowserPage(event.page))
                }

                is AboutEvent.OnNavigateToLicenses -> {
                    _effects.emit(AboutEffect.OnNavigateToLicenses)
                }

                is AboutEvent.OnNavigateToCredits -> {
                    _effects.emit(AboutEffect.OnNavigateToCredits)
                }

                is AboutEvent.OnNavigateBack -> {
                    _effects.emit(AboutEffect.OnNavigateBack)
                }
            }
        }
    }
}