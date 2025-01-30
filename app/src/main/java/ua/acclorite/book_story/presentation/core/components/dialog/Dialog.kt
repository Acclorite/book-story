package ua.acclorite.book_story.presentation.core.components.dialog

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.core.components.common.LazyColumnWithScrollbar


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dialog(
    modifier: Modifier = Modifier,
    icon: ImageVector? = null,
    title: String,
    description: String?,
    disableOnClick: Boolean = true,
    actionEnabled: Boolean?,
    onDismiss: () -> Unit,
    onAction: () -> Unit,
    withContent: Boolean,
    items: (LazyListScope.() -> Unit) = {}
) {
    var actionClicked by remember { mutableStateOf(false) }

    val imeInsets = WindowInsets.ime.asPaddingValues().let {
        it.calculateTopPadding() + it.calculateBottomPadding()
    }.value
    val screenHeightDp = LocalConfiguration.current.screenHeightDp

    val maxHeight = animateDpAsState(
        (screenHeightDp - imeInsets - 32).dp,
        animationSpec = spring(stiffness = Spring.StiffnessMedium)
    )

    BasicAlertDialog(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(0.dp, maxHeight.value)
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
        (LocalView.current.parent as DialogWindowProvider).window.setDimAmount(0.5f)
        Column {
            if (icon != null) {
                Icon(
                    imageVector = icon,
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
                        if (icon != null) Alignment.CenterHorizontally
                        else Alignment.Start
                    )
                    .padding(horizontal = 24.dp),
                textAlign = if (icon != null) TextAlign.Center
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
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }

            if (withContent) {
                Spacer(modifier = Modifier.height(24.dp))
            }

            LazyColumnWithScrollbar(
                modifier = Modifier.fillMaxWidth()
            ) {
                items()

                item {
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End)
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

                        TextButton(
                            onClick = {
                                if (disableOnClick) {
                                    actionClicked = true
                                }
                                onAction()
                            },
                            enabled = actionEnabled == true && !actionClicked
                        ) {
                            Text(
                                text = stringResource(id = R.string.ok),
                                style = MaterialTheme.typography.labelLarge,
                                color =
                                if (actionEnabled == true) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.primary.copy(0.5f)
                            )
                        }
                    }
                }
            }
        }
    }
}