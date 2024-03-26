package ua.acclorite.book_story.presentation.screens.help.components

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HelpClickableNote() {
    val context = LocalContext.current

    Column {
        Icon(
            imageVector = Icons.Outlined.Info,
            contentDescription = stringResource(id = R.string.note_content_desc),
            modifier = Modifier
                .size(20.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow(verticalArrangement = Arrangement.spacedBy(1.dp)) {
            Text(
                stringResource(id = R.string.clickable_note_1) + " ",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
            Text(
                stringResource(id = R.string.clickable_note_2) + " ",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable(
                        interactionSource = null,
                        indication = null,
                        onClick = {
                            Toast
                                .makeText(
                                    context,
                                    context.getString(R.string.clickable_note_action),
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    )
            )
            Text(
                stringResource(id = R.string.clickable_note_3),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }
    }

}






