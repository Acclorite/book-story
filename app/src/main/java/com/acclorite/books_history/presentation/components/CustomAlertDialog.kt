package com.acclorite.books_history.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import com.acclorite.books_history.R
import com.acclorite.books_history.ui.ElevationDefaults
import com.acclorite.books_history.ui.elevation

@Composable
fun CustomAlertDialog(
    modifier: Modifier = Modifier,
    properties: DialogProperties = DialogProperties(),
    drawableIcon: Painter? = null,
    imageVectorIcon: ImageVector? = null,
    backgroundTransparency: Float = 0.2f,
    title: String,
    description: String,
    actionText: String,
    onDismiss: () -> Unit,
    onAction: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        properties = properties,
        confirmButton = {
            TextButton(onClick = { onAction() }) {
                Text(
                    text = actionText,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        shape = MaterialTheme.shapes.extraLarge,
        modifier = modifier
            .navigationBarsPadding()
            .statusBarsPadding()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(MaterialTheme.elevation(ElevationDefaults.DialogElevation)),
        containerColor = Color.Transparent,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(
                    text = stringResource(id = R.string.cancel),
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        },
        icon = {
            if (drawableIcon != null) {
                Icon(
                    painter = drawableIcon,
                    contentDescription = title,
                    modifier = Modifier
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (imageVectorIcon != null) {
                Icon(
                    imageVector = imageVectorIcon,
                    contentDescription = title,
                    modifier = Modifier
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        },
        text = {
            (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(
                backgroundTransparency
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Start
            )
        }
    )
}

