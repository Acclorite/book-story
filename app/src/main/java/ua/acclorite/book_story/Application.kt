/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import ua.acclorite.book_story.core.crash.CrashHandler

@HiltAndroidApp
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        Thread.setDefaultUncaughtExceptionHandler(CrashHandler(this))
    }
}