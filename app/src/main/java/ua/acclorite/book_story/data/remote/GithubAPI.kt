/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.data.remote

import retrofit2.http.GET
import ua.acclorite.book_story.data.remote.dto.LatestReleaseInfo

interface GithubAPI {
    @GET("repos/Acclorite/book-story/releases/latest")
    suspend fun getLatestRelease(): LatestReleaseInfo

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}