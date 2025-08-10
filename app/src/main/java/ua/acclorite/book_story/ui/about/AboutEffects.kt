/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.about

import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.net.toUri
import kotlinx.coroutines.flow.SharedFlow
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.about.AboutEffect
import ua.acclorite.book_story.presentation.credits.CreditsScreen
import ua.acclorite.book_story.presentation.licenses.LicensesScreen
import ua.acclorite.book_story.ui.common.helpers.LocalActivity
import ua.acclorite.book_story.ui.common.helpers.launchActivity
import ua.acclorite.book_story.ui.common.helpers.showToast
import ua.acclorite.book_story.ui.navigator.LocalNavigator

@Composable
fun AboutEffects(effects: SharedFlow<AboutEffect>) {
    val navigator = LocalNavigator.current
    val activity = LocalActivity.current

    LaunchedEffect(effects, activity, navigator) {
        effects.collect { effect ->
            when (effect) {
                is AboutEffect.OnNavigateToBrowserPage -> {
                    val intent = Intent(
                        Intent.ACTION_VIEW,
                        effect.page.toUri()
                    )

                    intent.launchActivity(activity) {
                        activity.getString(R.string.error_no_browser)
                            .showToast(context = activity, longToast = false)
                    }
                }

                is AboutEffect.OnNavigateToLicenses -> {
                    navigator.push(LicensesScreen)
                }

                is AboutEffect.OnNavigateToCredits -> {
                    navigator.push(CreditsScreen)
                }

                is AboutEffect.OnNavigateBack -> {
                    navigator.pop()
                }
            }
        }
    }
}