/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import android.app.SearchManager
import android.content.Intent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.net.toUri
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.flow.SharedFlow
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.library.Book
import ua.acclorite.book_story.presentation.book_info.BookInfoScreen
import ua.acclorite.book_story.presentation.reader.ReaderEffect
import ua.acclorite.book_story.ui.common.helpers.LocalActivity
import ua.acclorite.book_story.ui.common.helpers.launchActivity
import ua.acclorite.book_story.ui.common.helpers.setBrightness
import ua.acclorite.book_story.ui.common.helpers.showToast
import ua.acclorite.book_story.ui.navigator.LocalNavigator

@Composable
fun ReaderEffects(
    effects: SharedFlow<ReaderEffect>,
    book: Book,
    fullscreen: Boolean
) {
    val navigator = LocalNavigator.current
    val activity = LocalActivity.current

    LaunchedEffect(effects, book, fullscreen) {
        effects.collect { effect ->
            when (effect) {
                is ReaderEffect.OnSystemBarsVisibility -> {
                    WindowCompat.getInsetsController(
                        activity.window,
                        activity.window.decorView
                    ).apply {
                        systemBarsBehavior = WindowInsetsControllerCompat
                            .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        if (effect.show ?: !fullscreen) show(WindowInsetsCompat.Type.systemBars())
                        else hide(WindowInsetsCompat.Type.systemBars())
                    }
                }

                is ReaderEffect.OnResetBrightness -> {
                    activity.setBrightness(brightness = null)
                }

                is ReaderEffect.OnOpenTranslator -> {
                    val translatorIntent = Intent()
                    val browserIntent = Intent()

                    translatorIntent.type = "text/plain"
                    translatorIntent.action = Intent.ACTION_PROCESS_TEXT
                    browserIntent.action = Intent.ACTION_WEB_SEARCH

                    translatorIntent.putExtra(
                        Intent.EXTRA_PROCESS_TEXT,
                        effect.textToTranslate
                    )
                    translatorIntent.putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, true)
                    browserIntent.putExtra(
                        SearchManager.QUERY,
                        "translate: ${effect.textToTranslate.trim()}"
                    )

                    translatorIntent.launchActivity(
                        activity = activity,
                        createChooser = !effect.translateWholeParagraph,
                        success = {
                            return@collect
                        }
                    )
                    browserIntent.launchActivity(
                        activity = activity,
                        success = {
                            return@collect
                        }
                    )

                    activity.getString(R.string.error_no_translator)
                        .showToast(context = activity, longToast = false)
                }

                is ReaderEffect.OnOpenShareApp -> {
                    val shareIntent = Intent()

                    shareIntent.action = Intent.ACTION_SEND
                    shareIntent.type = "text/plain"
                    shareIntent.putExtra(
                        Intent.EXTRA_SUBJECT,
                        activity.getString(R.string.app_name)
                    )
                    shareIntent.putExtra(
                        Intent.EXTRA_TEXT,
                        effect.textToShare.trim()
                    )

                    shareIntent.launchActivity(
                        activity = activity,
                        createChooser = true,
                        success = {
                            return@collect
                        }
                    )

                    activity.getString(R.string.error_no_share_app)
                        .showToast(context = activity, longToast = false)
                }

                is ReaderEffect.OnOpenWebBrowser -> {
                    val browserIntent = Intent()

                    browserIntent.action = Intent.ACTION_WEB_SEARCH
                    browserIntent.putExtra(
                        SearchManager.QUERY,
                        effect.textToSearch
                    )

                    browserIntent.launchActivity(
                        activity = activity,
                        success = {
                            return@collect
                        }
                    )

                    activity.getString(R.string.error_no_browser)
                        .showToast(context = activity, longToast = false)
                }

                is ReaderEffect.OnOpenDictionary -> {
                    val dictionaryIntent = Intent()
                    val browserIntent = Intent()

                    dictionaryIntent.type = "text/plain"
                    dictionaryIntent.action = Intent.ACTION_PROCESS_TEXT
                    dictionaryIntent.putExtra(
                        Intent.EXTRA_PROCESS_TEXT,
                        effect.textToDefine.trim()
                    )
                    dictionaryIntent.putExtra(Intent.EXTRA_PROCESS_TEXT_READONLY, true)

                    browserIntent.action = Intent.ACTION_VIEW
                    val text = effect.textToDefine.trim().replace(" ", "+")
                    browserIntent.data = "https://www.onelook.com/?w=$text".toUri()

                    dictionaryIntent.launchActivity(
                        activity = activity,
                        createChooser = true,
                        success = {
                            return@collect
                        }
                    )
                    browserIntent.launchActivity(
                        activity = activity,
                        success = {
                            return@collect
                        }
                    )

                    activity.getString(R.string.error_no_dictionary)
                        .showToast(context = activity, longToast = false)
                }

                is ReaderEffect.OnNavigateBack -> {
                    navigator.pop()
                }

                is ReaderEffect.OnNavigateToBookInfo -> {
                    if (effect.changePath) BookInfoScreen.changePathChannel.trySend(true)
                    navigator.push(
                        BookInfoScreen(
                            bookId = book.id
                        ),
                        popping = true,
                        saveInBackStack = false
                    )
                }
            }
        }
    }
}