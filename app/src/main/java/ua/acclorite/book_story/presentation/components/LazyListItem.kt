package ua.acclorite.book_story.presentation.components

import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.grid.LazyGridScope
import androidx.compose.runtime.Composable

fun <T> LazyListScope.customItems(
    items: List<T>,
    key: (T) -> Any?,
    content: @Composable LazyItemScope.(T) -> Unit
) {
    items.forEach {
        item(key = key(it)) {
            content(it)
        }
    }
}

fun <T> LazyListScope.customItemsIndexed(
    items: List<T>,
    key: (T) -> Any?,
    content: @Composable LazyItemScope.(Int, T) -> Unit
) {
    items.forEachIndexed { index, item ->
        item(key = key(item)) {
            content(index, item)
        }
    }
}

fun <T> LazyGridScope.customItems(
    items: List<T>,
    key: (T) -> Any?,
    content: @Composable LazyGridItemScope.(T) -> Unit
) {
    items.forEach {
        item(key = key(it)) {
            content(it)
        }
    }
}