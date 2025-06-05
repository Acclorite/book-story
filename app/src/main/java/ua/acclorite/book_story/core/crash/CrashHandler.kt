/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.crash

import android.content.Context
import android.content.Intent
import android.os.Process
import ua.acclorite.book_story.core.log.logE
import ua.acclorite.book_story.presentation.crash.CrashActivity
import kotlin.system.exitProcess

class CrashHandler(
    private val context: Context
) : Thread.UncaughtExceptionHandler {

    override fun uncaughtException(thread: Thread, throwable: Throwable) {
        val crashLog = throwable.stackTraceToString()

        logE(crashLog)
        if (!CrashUtils.saveCrashLog(context, crashLog)) return

        val intent = Intent(context, CrashActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        context.startActivity(intent)

        Process.killProcess(Process.myPid())
        exitProcess(1)
    }
}