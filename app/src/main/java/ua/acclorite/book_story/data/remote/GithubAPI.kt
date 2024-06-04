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