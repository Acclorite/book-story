/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.core.crash

import android.content.Context
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object CrashUtils {

    fun saveCrashLog(context: Context, crashLog: String): Boolean {
        val crashDir = File(context.filesDir, "crash")
        if (!crashDir.exists()) crashDir.mkdirs()

        val currentTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd_HH.mm.ss")
        val formattedDateTime = currentTime.format(formatter)

        val fileName = "crashLog_$formattedDateTime.txt"
        val crashFile = File(crashDir, fileName)
        val tempFile = File(crashDir, "$fileName.tmp")

        try {
            BufferedWriter(FileWriter(tempFile)).use { writer ->
                writer.write(crashLog)
            }

            if (!tempFile.renameTo(crashFile)) {
                throw IllegalStateException("Failed to rename .tmp file.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            tempFile.delete()
            return false
        }

        return true
    }

    fun getCrashLog(context: Context): String? {
        val crashDir = File(context.filesDir, "crash")
        if (!crashDir.exists()) return null

        val crashLogs = crashDir.listFiles() ?: return null
        val crashLog = crashLogs.maxByOrNull { it.lastModified() } ?: return null
        return crashLog.bufferedReader().use { reader ->
            reader.readText()
        }
    }
}