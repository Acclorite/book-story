package ua.acclorite.book_story.presentation.screens.settings.data

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Immutable
import ua.acclorite.book_story.domain.model.ColorPreset
import ua.acclorite.book_story.domain.util.Constants

@Immutable
data class SettingsState(
    val colorPresets: List<ColorPreset> = emptyList(),
    val selectedColorPreset: ColorPreset = Constants.DEFAULT_COLOR_PRESET,
    val animateColorPreset: Boolean = false,
    val colorPresetsListState: LazyListState = LazyListState()
)