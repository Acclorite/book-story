package com.acclorite.books_history.util

sealed class Resource<T>(val data: T? = null, val message: UIText? = null) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(message: UIText, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(val isLoading: Boolean = true) : Resource<T>()
}