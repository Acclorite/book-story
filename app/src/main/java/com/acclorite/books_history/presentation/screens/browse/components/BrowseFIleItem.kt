package com.acclorite.books_history.presentation.screens.browse.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.acclorite.books_history.R
import com.acclorite.books_history.ui.DefaultTransition
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BrowseFileItem(file: Pair<File, Boolean>, modifier: Modifier, onClick: () -> Unit) {
    val fileExtension: String = file.first.name.substringAfterLast(".", "")

    val pattern = SimpleDateFormat("HH:mm dd MMM", Locale.getDefault())

    val lastModified = pattern.format(Date(file.first.lastModified()))
    val icon = when (fileExtension) {
        "txt" -> painterResource(id = R.drawable.txt)
        "epub" -> painterResource(id = R.drawable.epub)
        "pdf" -> painterResource(id = R.drawable.pdf)
        else -> painterResource(id = R.drawable.file)
    }
    val outlineColor = if (file.second) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.outlineVariant
    val backgroundColor = if (file.second) MaterialTheme.colorScheme.secondaryContainer
    else Color.Transparent

    val animatedOutlineColor by animateColorAsState(
        targetValue = outlineColor,
        tween(300),
        label = "Outline animation"
    )
    val animatedBackgroundColor by animateColorAsState(
        targetValue = backgroundColor,
        tween(300),
        label = "Background animation"
    )

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(6.dp)
            .clip(MaterialTheme.shapes.medium)
            .border(
                width = 1.dp,
                color = animatedOutlineColor,
                shape = MaterialTheme.shapes.medium
            )
            .padding(1.dp)
            .background(animatedBackgroundColor)
            .clickable {
                onClick()
            }
    ) {
        Icon(
            painter = icon,
            contentDescription = "File icon",
            modifier = Modifier
                .padding(vertical = 60.dp)
                .size(70.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(horizontal = 10.dp, vertical = 12.dp)
        ) {
            Box {
                DefaultTransition(visible = !file.second) {
                    Icon(
                        painter = painterResource(R.drawable.file),
                        contentDescription = "File",
                        modifier = Modifier
                            .size(28.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                DefaultTransition(visible = file.second) {
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = "Checked",
                        modifier = Modifier
                            .size(28.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    file.first.name,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    lastModified,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.fillMaxWidth()
                )
            }

        }
    }
}