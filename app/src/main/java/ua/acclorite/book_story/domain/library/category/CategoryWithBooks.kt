package ua.acclorite.book_story.domain.library.category

import ua.acclorite.book_story.domain.library.book.SelectableBook
import ua.acclorite.book_story.domain.ui.UIText

data class CategoryWithBooks(
    val category: Category,
    val title: UIText,
    val books: List<SelectableBook>
)