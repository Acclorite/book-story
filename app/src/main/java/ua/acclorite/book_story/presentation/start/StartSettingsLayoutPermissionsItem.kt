package ua.acclorite.book_story.presentation.start

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R

@Composable
fun StartSettingsLayoutPermissionsItem(
    title: String,
    description: String,
    isOptional: Boolean,
    isGranted: Boolean,
    onGrantClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .padding(horizontal = 18.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium
                )

                if (!isOptional) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = stringResource(id = R.string.required_content_desc),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
            Text(
                text = description,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        if (!isGranted) {
            if (!isOptional) {
                Button(
                    onClick = onGrantClick,
                    contentPadding = ButtonDefaults.TextButtonContentPadding
                ) {
                    Text(
                        text = stringResource(id = R.string.grant),
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            } else {
                TextButton(onClick = onGrantClick) {
                    Text(text = stringResource(id = R.string.grant))
                }
            }
        } else {
            Text(
                text = stringResource(id = R.string.start_permissions_granted),
                color = MaterialTheme.colorScheme.primary.copy(0.7f),
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(ButtonDefaults.TextButtonContentPadding)
            )
        }
    }
}