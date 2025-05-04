/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.common.components.dialog

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import ua.acclorite.book_story.R
import ua.acclorite.book_story.ui.common.components.common.LazyColumnWithScrollbar
import ua.acclorite.book_story.ui.common.components.common.StyledText


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
    secondaryAction: String? = null,
    onSecondaryAction: (() -> Unit)? = null,
    withContent: Boolean,
    items: (LazyListScope.() -> Unit) = {}
) {
    var actionClicked by remember { mutableStateOf(false) }

    val imeInsets = WindowInsets.ime.asPaddingValues().let {
        it.calculateTopPadding() + it.calculateBottomPadding()
    }.value
    val screenHeightDp = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.height.toDp().value
    }

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

            StyledText(
                text = title,
                modifier = Modifier
                    .align(
                        if (icon != null) Alignment.CenterHorizontally
                        else Alignment.Start
                    )
                    .padding(horizontal = 24.dp),
                style = MaterialTheme.typography.headlineSmall.copy(
                    textAlign = if (icon != null) TextAlign.Center
                    else TextAlign.Start,
                    color = MaterialTheme.colorScheme.onSurface
                )
            )

            if (description != null) {
                Spacer(modifier = Modifier.height(16.dp))
                StyledText(
                    text = description,
                    modifier = Modifier.padding(horizontal = 24.dp),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
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
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (secondaryAction != null && onSecondaryAction != null) {
                            TextButton(
                                onClick = {
                                    if (disableOnClick) {
                                        actionClicked = true
                                    }
                                    onSecondaryAction()
                                },
                                enabled = !actionClicked
                            ) {
                                StyledText(
                                    text = secondaryAction,
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.weight(1f),
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.End),
                            verticalAlignment = Alignment.CenterVertically
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
                                StyledText(
                                    text = stringResource(id = R.string.cancel),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = MaterialTheme.colorScheme.primary
                                    )
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
                                StyledText(
                                    text = stringResource(id = R.string.ok),
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        color = if (actionEnabled == true) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.primary.copy(0.5f)
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}