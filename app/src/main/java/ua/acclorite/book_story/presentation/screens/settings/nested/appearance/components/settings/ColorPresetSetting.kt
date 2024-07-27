package ua.acclorite.book_story.presentation.screens.settings.nested.appearance.components.settings

import android.content.Context
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.rounded.DragHandle
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import sh.calvin.reorderable.ReorderableCollectionItemScope
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ColorPreset
import ua.acclorite.book_story.domain.util.Selected
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.components.customItemsIndexed
import ua.acclorite.book_story.presentation.screens.settings.components.ColorPickerWithTitle
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsEvent
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsState
import ua.acclorite.book_story.presentation.ui.DefaultTransition
import ua.acclorite.book_story.presentation.ui.Transitions

/**
 * Color Preset setting.
 * Lets user create and use custom color preset.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.ColorPresetSetting(
    state: State<SettingsState>,
    onEvent: (SettingsEvent) -> Unit,
    backgroundColor: Color,
    verticalPadding: Dp = 8.dp,
    horizontalPadding: Dp = 18.dp
) {
    val context = LocalContext.current
    val reorderableListState = rememberReorderableLazyListState(
        lazyListState = state.value.colorPresetsListState
    ) { from, to ->
        onEvent(SettingsEvent.OnReorderColorPresets(from.index, to.index))
    }

    Column(
        Modifier
            .animateItem()
            .fillMaxWidth()
            .padding(vertical = verticalPadding)
    ) {
        CategoryTitle(
            title = stringResource(id = R.string.color_preset_option),
            padding = horizontalPadding
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            state = state.value.colorPresetsListState,
            verticalAlignment = Alignment.CenterVertically
        ) {
            customItemsIndexed(
                state.value.colorPresets,
                key = { item, _ -> item.id }
            ) { index, colorPreset ->
                ReorderableItem(
                    state = reorderableListState,
                    animateItemModifier = Modifier,
                    key = colorPreset.id
                ) {
                    Row {
                        if (index == 0) {
                            Spacer(modifier = Modifier.width(horizontalPadding))
                        } else {
                            Spacer(modifier = Modifier.width(2.dp))
                        }

                        ColorPresetSettingItem(
                            colorPreset = colorPreset,
                            isSelected = remember(
                                colorPreset.isSelected,
                                state.value.colorPresets.size
                            ) {
                                if (state.value.colorPresets.size > 1) {
                                    colorPreset.isSelected
                                } else true
                            },
                            enableAnimation = state.value.animateColorPreset,
                            context = context,
                            canDrag = remember(state.value.colorPresets.size) {
                                state.value.colorPresets.size > 1
                            },
                            onDragStopped = {
                                onEvent(SettingsEvent.OnConfirmReorderColorPresets)
                            },
                            onClick = {
                                onEvent(
                                    SettingsEvent.OnSelectColorPreset(
                                        id = colorPreset.id
                                    )
                                )
                            }
                        )

                        if (index == state.value.colorPresets.lastIndex) {
                            Spacer(modifier = Modifier.width(horizontalPadding))
                        } else {
                            Spacer(modifier = Modifier.width(2.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Column(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
                .clip(MaterialTheme.shapes.large)
                .background(
                    backgroundColor,
                    MaterialTheme.shapes.large
                )
        ) {
            Spacer(modifier = Modifier.height(18.dp))

            ColorPresetSettingConfigurationItem(
                selectedColorPreset = state.value.selectedColorPreset,
                canDelete = state.value.colorPresets.size > 1,
                onDelete = {
                    onEvent(
                        SettingsEvent.OnDeleteColorPreset(
                            id = state.value.selectedColorPreset.id
                        )
                    )
                },
                onTitleChange = {
                    onEvent(
                        SettingsEvent.OnUpdateColorPresetTitle(
                            id = state.value.selectedColorPreset.id,
                            title = it
                        )
                    )
                },
                onShuffle = {
                    onEvent(
                        SettingsEvent.OnShuffleColorPreset(
                            id = state.value.selectedColorPreset.id
                        )
                    )
                },
                onAdd = {
                    onEvent(SettingsEvent.OnAddColorPreset)
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            ColorPickerWithTitle(
                value = state.value.selectedColorPreset.backgroundColor,
                presetId = state.value.selectedColorPreset.id,
                title = stringResource(id = R.string.background_color_option),
                onValueChange = {
                    onEvent(
                        SettingsEvent.OnUpdateColorPresetColor(
                            id = state.value.selectedColorPreset.id,
                            backgroundColor = it,
                            fontColor = null
                        )
                    )
                }
            )
            ColorPickerWithTitle(
                value = state.value.selectedColorPreset.fontColor,
                presetId = state.value.selectedColorPreset.id,
                title = stringResource(id = R.string.font_color_option),
                onValueChange = {
                    onEvent(
                        SettingsEvent.OnUpdateColorPresetColor(
                            id = state.value.selectedColorPreset.id,
                            backgroundColor = null,
                            fontColor = it
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

/**
 * Color Preset Setting Item.
 * Lets user select color preset and shows little preview of the preset.
 *
 * @param colorPreset [ColorPreset] to select.
 * @param isSelected Whether [ColorPreset] is selected.
 * @param canDrag Whether user can drag [ColorPreset].
 * @param enableAnimation Whether there is appearing/disappearing animation.
 * @param context [Context].
 * @param onDragStopped Callback when user stops dragging [ColorPreset].
 * @param onClick OnClick callback.
 */
@Composable
private fun ReorderableCollectionItemScope.ColorPresetSettingItem(
    colorPreset: ColorPreset,
    isSelected: Selected,
    canDrag: Boolean,
    enableAnimation: Boolean,
    context: Context,
    onDragStopped: () -> Unit,
    onClick: () -> Unit
) {
    val title = remember(colorPreset) {
        if ((colorPreset.name ?: "").isBlank()) {
            return@remember context.getString(
                R.string.color_preset_query,
                colorPreset.id.toString()
            )
        }

        colorPreset.name!!
    }

    val borderColor = remember(isSelected, colorPreset.fontColor) {
        if (!isSelected) colorPreset.fontColor.copy(0.3f)
        else colorPreset.fontColor
    }
    val animatedBorderColor = animateColorAsState(
        borderColor,
        label = ""
    )

    val animatedBackgroundColor = animateColorAsState(
        colorPreset.backgroundColor,
        label = ""
    )
    val animatedFontColor = animateColorAsState(
        colorPreset.fontColor,
        label = ""
    )

    Row(
        Modifier
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = if (enableAnimation) animatedBorderColor.value
                else borderColor,
                shape = CircleShape
            )
            .padding(2.dp)
            .background(animatedBackgroundColor.value, CircleShape)
            .clickable(enabled = !isSelected) {
                onClick()
            }
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomAnimatedVisibility(
            visible = isSelected,
            enter = if (enableAnimation) expandHorizontally() + fadeIn()
            else Transitions.NoEnterAnimation,
            exit = if (enableAnimation) shrinkHorizontally() + fadeOut()
            else Transitions.NoExitAnimation
        ) {
            Icon(
                imageVector = Icons.Default.Done,
                contentDescription = stringResource(id = R.string.selected_content_desc),
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(18.dp),
                tint = colorPreset.fontColor
            )
        }

        Text(
            title.trim(),
            color = animatedFontColor.value,
            style = MaterialTheme.typography.labelLarge,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1
        )

        CustomAnimatedVisibility(
            visible = isSelected && canDrag,
            enter = if (enableAnimation) expandHorizontally() + fadeIn()
            else Transitions.NoEnterAnimation,
            exit = if (enableAnimation) shrinkHorizontally() + fadeOut()
            else Transitions.NoExitAnimation
        ) {
            Icon(
                imageVector = Icons.Rounded.DragHandle,
                contentDescription = stringResource(id = R.string.drag_content_desc),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(18.dp)
                    .longPressDraggableHandle(
                        onDragStopped = onDragStopped
                    ),
                tint = colorPreset.fontColor
            )
        }
    }
}

/**
 * Color Preset Settings Configuration Item.
 * Lets user configure Title, Shuffle colors, Delete presets and Add presets.
 *
 * @param selectedColorPreset Currently selected [ColorPreset].
 * @param canDelete Whether this [ColorPreset] can be deleted.
 * @param onDelete Delete callback.
 * @param onShuffle Shuffle callback.
 * @param onTitleChange TitleChange callback.
 * @param onAdd Add callback.
 */
@OptIn(FlowPreview::class)
@Composable
private fun ColorPresetSettingConfigurationItem(
    selectedColorPreset: ColorPreset,
    canDelete: Boolean,
    onDelete: () -> Unit,
    onShuffle: () -> Unit,
    onTitleChange: (String) -> Unit,
    onAdd: () -> Unit
) {
    val title = remember(selectedColorPreset.id) {
        mutableStateOf(selectedColorPreset.name ?: "")
    }

    LaunchedEffect(title) {
        snapshotFlow {
            title.value
        }.debounce(50).collectLatest {
            onTitleChange(it)
        }
    }

    Row(
        Modifier.padding(horizontal = 18.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BasicTextField(
            value = title.value,
            singleLine = true,
            modifier = Modifier.weight(1f),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                lineHeight = MaterialTheme.typography.titleLarge.lineHeight,
                fontFamily = MaterialTheme.typography.titleLarge.fontFamily
            ),
            onValueChange = {
                if (it.length < 40 || it.length <= title.value.length) {
                    title.value = it
                }
            },
            keyboardOptions = KeyboardOptions(
                KeyboardCapitalization.Sentences
            ),
            cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant)
        ) { innerText ->
            Box(
                modifier = Modifier.fillMaxHeight(),
                contentAlignment = Alignment.CenterStart
            ) {
                if (title.value.isEmpty()) {
                    Text(
                        stringResource(id = R.string.color_preset_placeholder),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                innerText()
            }
        }

        Spacer(modifier = Modifier.width(24.dp))

        DefaultTransition(visible = canDelete) {
            CustomIconButton(
                modifier = Modifier.size(24.dp),
                icon = Icons.Default.DeleteOutline,
                contentDescription = R.string.delete_color_preset_content_desc,
                disableOnClick = false,
                enabled = canDelete,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                onDelete()
            }
        }

        CustomIconButton(
            modifier = Modifier.size(24.dp),
            icon = Icons.Default.Shuffle,
            contentDescription = R.string.shuffle_color_preset_content_desc,
            disableOnClick = false,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            onShuffle()
        }

        CustomIconButton(
            modifier = Modifier.size(24.dp),
            icon = Icons.Default.Add,
            contentDescription = R.string.create_color_preset_content_desc,
            disableOnClick = false,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ) {
            onAdd()
        }
    }
}