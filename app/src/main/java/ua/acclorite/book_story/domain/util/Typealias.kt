package ua.acclorite.book_story.domain.util

import android.graphics.Bitmap
import ua.acclorite.book_story.presentation.data.Navigator

typealias CoverImage = Bitmap
typealias Selected = Boolean
typealias ID = Int
typealias Route = String
typealias OnNavigate = (Navigator.() -> Unit) -> Unit
