package com.acclorite.books_history.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import java.util.UUID

@Immutable
data class FormattedLine(
    val line: String,
    val style: TextStyle = TextStyle(),
    val textSize: TextUnit = 0.sp,
    val id: String = UUID.randomUUID().toString()
)
