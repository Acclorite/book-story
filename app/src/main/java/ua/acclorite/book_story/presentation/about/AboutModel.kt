/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.about

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.helpers.launchActivity
import ua.acclorite.book_story.ui.common.helpers.showToast
import javax.inject.Inject

@HiltViewModel
class AboutModel @Inject constructor() : ViewModel() {

    fun onEvent(event: AboutEvent) {
        when (event) {
            is AboutEvent.OnNavigateToBrowserPage -> {
                viewModelScope.launch {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        event.page.toUri()
                    )

                    intent.launchActivity(event.context as ComponentActivity) {
                        withContext(Dispatchers.Main) {
                            event.context.getString(R.string.error_no_browser)
                                .showToast(context = event.context, longToast = false)
                        }
                    }
                }
            }
        }
    }
}