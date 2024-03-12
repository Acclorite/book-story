package ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.theme_switcher

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.ui.Theme
import ua.acclorite.book_story.ui.colorScheme
import ua.acclorite.book_story.util.UIText

/**
 * Theme switcher item.
 */
@Composable
fun AppearanceSettingsThemeSwitcherItem(
    theme: Pair<Theme, UIText>,
    darkTheme: Boolean,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = colorScheme(theme.first, darkTheme)

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            Modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.large)
                .clickable(enabled = !selected) {
                    onClick()
                }
                .background(
                    colorScheme.surface
                )
                .border(
                    4.dp,
                    if (selected) colorScheme.primary
                    else MaterialTheme.colorScheme.outlineVariant,
                    MaterialTheme.shapes.large
                )
                .padding(4.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .height(20.dp)
                        .width(80.dp)
                        .background(colorScheme.onSurface)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .size(26.dp),
                    tint =
                    if (selected) colorScheme.primary
                    else Color.Transparent
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                Modifier
                    .padding(start = 8.dp)
                    .height(80.dp)
                    .width(70.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(
                        colorScheme.surfaceColorAtElevation(
                            NavigationBarDefaults.Elevation
                        )
                    )
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                Modifier
                    .width(130.dp)
                    .height(40.dp)
                    .background(
                        colorScheme.surfaceColorAtElevation(
                            NavigationBarDefaults.Elevation
                        )
                    ),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .clip(CircleShape)
                        .size(20.dp)
                        .background(colorScheme.primary)
                )
                Box(
                    Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .height(20.dp)
                        .width(60.dp)
                        .background(colorScheme.onSurfaceVariant)
                )

            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = theme.second.asString(),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )
    }
}