package ua.acclorite.book_story.data.remote.dto

import androidx.annotation.Keep
import androidx.compose.runtime.Immutable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@Keep
@JsonClass(generateAdapter = true)
@Immutable
data class LatestReleaseInfo(
    @Json(name = "name") val name: String,
    @Json(name = "tag_name") val tagName: String
)
