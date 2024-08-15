package ua.acclorite.book_story.presentation.components.custom_dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import ua.acclorite.book_story.R


/**
 * Custom Dialog with lazy column.
 *
 * @param modifier Modifier to be applied.
 * @param drawableIcon Drawable icon to show(1/2).
 * @param imageVectorIcon Image vector icon to show(2/2).
 * @param backgroundTransparency The transparency of the background behind the dialog.
 * @param title The title of the dialog.
 * @param description The description of the dialog.
 * @param actionText The action text of the dialog.
 * @param disableOnClick Whether should all actions be disabled after clicking it.
 * @param isActionEnabled Whether action is enabled or not.
 * @param onDismiss Code to execute when the dialog is dismissed.
 * @param onAction Code to execute when action is clicked.
 * @param withDivider Should divider be shown or not.
 * @param items The items of this dialog, use items(..) { .. } or item { .. }.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDialogWithLazyColumn(
    modifier: Modifier = Modifier,
    drawableIcon: Painter? = null,
    imageVectorIcon: ImageVector? = null,
    backgroundTransparency: Float = 0.5f,
    title: String,
    description: String?,
    actionText: String?,
    disableOnClick: Boolean = true,
    isActionEnabled: Boolean?,
    onDismiss: () -> Unit,
    onAction: () -> Unit,
    withDivider: Boolean,
    items: (LazyListScope.() -> Unit) = {}
) {
    var actionClicked by remember { mutableStateOf(false) }
    BasicAlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .statusBarsPadding()
            .clip(MaterialTheme.shapes.extraLarge)
            .background(
                MaterialTheme.colorScheme.surfaceContainerHigh,
                MaterialTheme.shapes.extraLarge
            )
            .padding(top = 24.dp, bottom = 12.dp),
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = !actionClicked,
            dismissOnClickOutside = !actionClicked
        )
    ) {
        (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(
            backgroundTransparency
        )
        Column {
            if (drawableIcon != null) {
                Icon(
                    painter = drawableIcon,
                    contentDescription = title,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
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
                        .align(Alignment.CenterHorizontally)
                        .size(24.dp),
                    tint = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            Text(
                text = title,
                modifier = Modifier
                    .align(
                        if (drawableIcon != null || imageVectorIcon != null) Alignment.CenterHorizontally
                        else Alignment.Start
                    )
                    .padding(horizontal = 24.dp),
                textAlign = if (drawableIcon != null || imageVectorIcon != null) TextAlign.Center
                else TextAlign.Start,
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            if (description != null) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Start,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
            if (withDivider) {
                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    color = MaterialTheme.colorScheme.outlineVariant
                )
                Spacer(modifier = Modifier.height(12.dp))
            } else {
                Spacer(modifier = Modifier.height(8.dp))
            }

            LazyColumn {
                items()

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(
                            modifier = Modifier
                                .align(Alignment.End)
                                .padding(horizontal = 24.dp)
                        ) {
                            TextButton(
                                onClick = {
                                    if (disableOnClick) {
                                        actionClicked = true
                                    }
                                    onDismiss()
                                },
                                enabled = !actionClicked
                            ) {
                                Text(
                                    text = stringResource(id = R.string.cancel),
                                    style = MaterialTheme.typography.labelLarge,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            if (actionText != null) {
                                Spacer(modifier = Modifier.width(4.dp))
                                TextButton(
                                    onClick = {
                                        if (disableOnClick) {
                                            actionClicked = true
                                        }
                                        onAction()
                                    },
                                    enabled = isActionEnabled == true && !actionClicked
                                ) {
                                    Text(
                                        text = actionText,
                                        style = MaterialTheme.typography.labelLarge,
                                        color =
                                        if (isActionEnabled == true) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.primary.copy(0.5f)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}