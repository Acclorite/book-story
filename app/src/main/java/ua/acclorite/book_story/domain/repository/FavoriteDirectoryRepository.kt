package ua.acclorite.book_story.domain.repository

interface FavoriteDirectoryRepository {

    suspend fun updateFavoriteDirectory(path: String)
}