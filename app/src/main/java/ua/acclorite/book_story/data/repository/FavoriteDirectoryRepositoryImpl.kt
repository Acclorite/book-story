package ua.acclorite.book_story.data.repository

import ua.acclorite.book_story.data.local.dto.FavoriteDirectoryEntity
import ua.acclorite.book_story.data.local.room.BookDao
import ua.acclorite.book_story.domain.repository.FavoriteDirectoryRepository
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Favorite Directory repository.
 * Manages all [ua.acclorite.book_story.data.local.dto.FavoriteDirectoryEntity] related work.
 */
@Singleton
class FavoriteDirectoryRepositoryImpl @Inject constructor(
    private val database: BookDao,
) : FavoriteDirectoryRepository {

    /**
     * Create or delete favorite directory if already exists.
     *
     * @param path Path to directory.
     */
    override suspend fun updateFavoriteDirectory(path: String) {
        if (database.favoriteDirectoryExits(path)) {
            database.deleteFavoriteDirectory(
                FavoriteDirectoryEntity(path)
            )
            return
        }

        database.insertFavoriteDirectory(
            FavoriteDirectoryEntity(path)
        )
    }
}