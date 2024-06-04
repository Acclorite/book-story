package ua.acclorite.book_story.presentation.data

fun String.removeTrailingZero(): String {
    if (!this.contains('.'))
        return this
    return this
        .dropLastWhile { it == '0' }
        .dropLastWhile { it == '.' }
}

fun Double.removeDigits(digits: Int) = "%.${digits}f".format(this).replace(",", ".")

fun <K, T> Map<K, T>.update(item: Pair<K, T>): Map<K, T> {
    val keyExists = containsKey(item.first)

    return if (!keyExists) this
    else this.plus(item)
}

fun <K, T> Map<K, T>.update(items: List<Pair<K, T>>): Map<K, T> {
    var map = this

    for (item in items) {
        map = map.update(item)
    }

    return map
}

fun <K, T> Map<K, T>.update(items: Map<K, T>): Map<K, T> {
    var map = this

    for (item in items) {
        map = map.update(item.toPair())
    }

    return map
}

fun <K, T> Map<K, T>.updateWithCopy(
    calculation: (T) -> T
): Map<K, T> {
    var map = this

    for (item in this) {
        map = map.update(item.key to calculation(item.value))
    }

    return map
}

fun <K, T> Map<K, T>.removeKeys(
    keys: Set<K>
): Map<K, T> {
    val map = this.toMutableMap()

    for (key in keys) {
        map.remove(key)
    }

    return map.toMap()
}

fun <K, T> Map<K, T>.removeKey(
    key: K
): Map<K, T> {
    val map = this.toMutableMap()
    map.remove(key)
    return map.toMap()
}