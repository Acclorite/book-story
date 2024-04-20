package ua.acclorite.book_story.data.remote

import retrofit2.http.GET
import ua.acclorite.book_story.data.remote.dto.ReleaseResponse

interface GithubAPI {
    @GET("repos/Acclorite/book-story/releases/latest")
    suspend fun getLatestRelease(): ReleaseResponse

    companion object {
        const val BASE_URL = "https://api.github.com/"
    }
}