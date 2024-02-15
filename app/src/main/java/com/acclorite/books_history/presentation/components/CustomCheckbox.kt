package com.acclorite.books_history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.acclorite.books_history.ui.elevation

@Composable
fun CustomCheckbox(selected: Boolean) {
    Icon(
        imageVector = Icons.Default.Check,
        tint = if (selected) MaterialTheme.elevation(elevation = 2.dp) else Color.Transparent,
        modifier = Modifier
            .clip(CircleShape)
            .border(
                width = if (!selected) 1.dp else 0.dp,
                if (!selected) MaterialTheme.colorScheme.outline else Color.Transparent,
                CircleShape
            )
            .background(
                if (selected) MaterialTheme.colorScheme.primary else Color.Transparent,
                shape = CircleShape
            )
            .padding(4.dp)
            .size(22.dp),
        contentDescription = "checkbox"
    )
}