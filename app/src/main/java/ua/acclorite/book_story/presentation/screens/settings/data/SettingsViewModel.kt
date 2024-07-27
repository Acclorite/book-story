package ua.acclorite.book_story.presentation.screens.settings.data

import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.ColorPreset
import ua.acclorite.book_story.domain.use_case.DeleteColorPreset
import ua.acclorite.book_story.domain.use_case.GetColorPresets
import ua.acclorite.book_story.domain.use_case.ReorderColorPresets
import ua.acclorite.book_story.domain.use_case.SelectColorPreset
import ua.acclorite.book_story.domain.use_case.UpdateColorPreset
import ua.acclorite.book_story.domain.util.Constants
import ua.acclorite.book_story.domain.util.ID
import ua.acclorite.book_story.domain.util.UIText
import ua.acclorite.book_story.presentation.data.launchActivity
import javax.inject.Inject
import kotlin.random.Random

@OptIn(ExperimentalPermissionsApi::class)
@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getColorPresets: GetColorPresets,
    private val updateColorPreset: UpdateColorPreset,
    private val deleteColorPreset: DeleteColorPreset,
    private val selectColorPreset: SelectColorPreset,
    private val reorderColorPresets: ReorderColorPresets
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private var notificationsPermissionJob: Job? = null
    private var selectColorPresetJob: Job? = null
    private var addColorPresetJob: Job? = null
    private var deleteColorPresetJob: Job? = null
    private var updateColorColorPresetJob: Job? = null
    private var updateTitleColorPresetJob: Job? = null
    private var shuffleColorPresetJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            var colorPresets = getColorPresets.execute()

            if (colorPresets.isEmpty()) {
                updateColorPreset.execute(Constants.DEFAULT_COLOR_PRESET)
                onSelectColorPreset(getColorPresets.execute().first())
                colorPresets = getColorPresets.execute()
            }

            val scrollIndex = colorPresets.indexOfFirst {
                it.isSelected
            }
            if (scrollIndex != -1) {
                launch(Dispatchers.Main) {
                    try {
                        _state.value.colorPresetsListState.requestScrollToItem(
                            index = scrollIndex
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            _state.update {
                it.copy(
                    selectedColorPreset = selectedColorPreset(colorPresets),
                    colorPresets = colorPresets
                )
            }
            _isReady.update {
                true
            }
        }
    }

    @Suppress("LABEL_NAME_CLASH")
    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnGeneralChangeCheckForUpdates -> {
                if (!event.enable) {
                    event.onChangeCheckForUpdates(false)
                    return
                }

                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                    event.onChangeCheckForUpdates(true)
                    return
                }

                if (event.notificationsPermissionState.status.isGranted) {
                    event.onChangeCheckForUpdates(true)
                    return
                }

                if (!event.notificationsPermissionState.status.shouldShowRationale) {
                    event.notificationsPermissionState.launchPermissionRequest()
                } else {
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, event.activity.packageName)

                    var failure = false
                    intent.launchActivity(event.activity) {
                        failure = true
                    }

                    if (failure) {
                        return
                    }
                }

                notificationsPermissionJob?.cancel()
                notificationsPermissionJob = viewModelScope.launch {
                    for (i in 1..10) {
                        if (!event.notificationsPermissionState.status.isGranted) {
                            delay(1000)
                            yield()
                            continue
                        }

                        yield()

                        event.onChangeCheckForUpdates(true)
                        break
                    }
                }
            }

            is SettingsEvent.OnSelectPreviousPreset -> {
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    selectColorPresetJob = launch {
                        val colorPresets = _state.value.colorPresets
                        val selectedPreset = _state.value.selectedColorPreset

                        if (colorPresets.size == 1) {
                            return@launch
                        }

                        val selectedPresetIndex = colorPresets.indexOf(selectedPreset)
                        if (selectedPresetIndex == -1) {
                            return@launch
                        }

                        val previousColorPresetIndex = when (selectedPresetIndex) {
                            0 -> {
                                colorPresets.lastIndex
                            }

                            else -> {
                                selectedPresetIndex - 1
                            }
                        }
                        val previousColorPreset = colorPresets.getOrNull(previousColorPresetIndex)
                            ?: return@launch

                        yield()

                        onSelectColorPreset(previousColorPreset)
                        val updatedColorPresets = _state.value.colorPresets.map {
                            it.copy(isSelected = previousColorPreset.id == it.id)
                        }
                        _state.update {
                            it.copy(
                                selectedColorPreset = selectedColorPreset(updatedColorPresets),
                                colorPresets = updatedColorPresets
                            )
                        }

                        event.onSelected(
                            if ((previousColorPreset.name ?: "").isBlank()) {
                                UIText.StringResource(
                                    R.string.color_preset_query,
                                    previousColorPreset.id.toString()
                                )
                            } else {
                                UIText.StringValue(previousColorPreset.name!!)
                            }
                        )
                    }
                }
            }

            is SettingsEvent.OnSelectNextPreset -> {
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    selectColorPresetJob = launch {
                        val colorPresets = _state.value.colorPresets
                        val selectedPreset = _state.value.selectedColorPreset

                        if (colorPresets.size == 1) {
                            return@launch
                        }

                        val selectedPresetIndex = colorPresets.indexOf(selectedPreset)
                        if (selectedPresetIndex == -1) {
                            return@launch
                        }

                        val nextColorPresetIndex = when (selectedPresetIndex) {
                            colorPresets.lastIndex -> {
                                0
                            }

                            else -> {
                                selectedPresetIndex + 1
                            }
                        }
                        val nextColorPreset = colorPresets.getOrNull(nextColorPresetIndex)
                            ?: return@launch

                        yield()

                        onSelectColorPreset(nextColorPreset)
                        val updatedColorPresets = _state.value.colorPresets.map {
                            it.copy(isSelected = nextColorPreset.id == it.id)
                        }
                        _state.update {
                            it.copy(
                                selectedColorPreset = selectedColorPreset(updatedColorPresets),
                                colorPresets = updatedColorPresets
                            )
                        }

                        event.onSelected(
                            if ((nextColorPreset.name ?: "").isBlank()) {
                                UIText.StringResource(
                                    R.string.color_preset_query,
                                    nextColorPreset.id.toString()
                                )
                            } else {
                                UIText.StringValue(nextColorPreset.name!!)
                            }
                        )
                    }
                }
            }

            is SettingsEvent.OnSelectColorPreset -> {
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    selectColorPresetJob = launch {
                        val colorPreset = getColorPresetById(event.id) ?: return@launch

                        yield()

                        onSelectColorPreset(colorPreset, animate = true)
                        val colorPresets = _state.value.colorPresets.map {
                            it.copy(isSelected = colorPreset.id == it.id)
                        }
                        _state.update {
                            it.copy(
                                selectedColorPreset = selectedColorPreset(colorPresets),
                                colorPresets = colorPresets
                            )
                        }
                    }
                }
            }

            is SettingsEvent.OnDeleteColorPreset -> {
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    deleteColorPresetJob = launch {
                        val colorPreset = getColorPresetById(event.id) ?: return@launch

                        yield()

                        val position = _state.value.colorPresets.indexOf(colorPreset)
                        if (position == -1) {
                            return@launch
                        }

                        val nextPosition = if (position == _state.value.colorPresets.lastIndex) {
                            position - 1
                        } else {
                            position
                        }

                        yield()

                        deleteColorPreset.execute(colorPreset)
                        onSelectColorPreset(getColorPresets.execute()[nextPosition])
                        val colorPresets = getColorPresets.execute()

                        _state.update {
                            it.copy(
                                selectedColorPreset = selectedColorPreset(colorPresets),
                                colorPresets = colorPresets
                            )
                        }

                        onEvent(SettingsEvent.OnScrollToColorPreset(nextPosition))
                    }
                }
            }

            is SettingsEvent.OnScrollToColorPreset -> {
                viewModelScope.launch {
                    try {
                        _state.value.colorPresetsListState.requestScrollToItem(
                            event.scrollTo
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            is SettingsEvent.OnUpdateColorPresetTitle -> {
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    updateTitleColorPresetJob = launch {
                        val colorPreset = getColorPresetById(event.id) ?: return@launch

                        yield()

                        val updatedColorPreset = colorPreset.copy(
                            name = event.title
                        )

                        yield()

                        updateColorPreset.execute(updatedColorPreset)
                        _state.update {
                            it.copy(
                                selectedColorPreset = updatedColorPreset,
                                colorPresets = it.colorPresets.updateColorPreset(
                                    updatedColorPreset
                                )
                            )
                        }
                    }
                }
            }

            is SettingsEvent.OnShuffleColorPreset -> {
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    shuffleColorPresetJob = launch {
                        val colorPreset = getColorPresetById(event.id) ?: return@launch

                        yield()

                        val shuffledColorPreset = colorPreset.copy(
                            backgroundColor = colorPreset.backgroundColor.copy(
                                red = Random.nextFloat(),
                                green = Random.nextFloat(),
                                blue = Random.nextFloat()
                            ),
                            fontColor = colorPreset.fontColor.copy(
                                red = Random.nextFloat(),
                                green = Random.nextFloat(),
                                blue = Random.nextFloat()
                            )
                        )

                        yield()

                        updateColorPreset.execute(shuffledColorPreset)
                        _state.update {
                            it.copy(
                                selectedColorPreset = shuffledColorPreset,
                                colorPresets = it.colorPresets.updateColorPreset(
                                    shuffledColorPreset
                                )
                            )
                        }
                    }
                }
            }

            is SettingsEvent.OnAddColorPreset -> {
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    addColorPresetJob = launch {
                        yield()

                        val newColorPreset = Constants.DEFAULT_COLOR_PRESET
                        updateColorPreset.execute(newColorPreset)

                        onSelectColorPreset(getColorPresets.execute().last())
                        val colorPresets = getColorPresets.execute()

                        _state.update {
                            it.copy(
                                selectedColorPreset = selectedColorPreset(colorPresets),
                                colorPresets = colorPresets
                            )
                        }

                        onEvent(SettingsEvent.OnScrollToColorPreset(colorPresets.lastIndex))
                    }
                }
            }

            is SettingsEvent.OnUpdateColorPresetColor -> {
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    updateColorColorPresetJob = launch {
                        val colorPreset = getColorPresetById(event.id) ?: return@launch

                        yield()

                        val updatedColorPreset = colorPreset.copy(
                            backgroundColor = event.backgroundColor
                                ?: colorPreset.backgroundColor,
                            fontColor = event.fontColor
                                ?: colorPreset.fontColor
                        )

                        yield()

                        updateColorPreset.execute(updatedColorPreset)
                        _state.update {
                            it.copy(
                                selectedColorPreset = updatedColorPreset,
                                colorPresets = it.colorPresets.updateColorPreset(
                                    updatedColorPreset
                                )
                            )
                        }
                    }
                }
            }

            is SettingsEvent.OnReorderColorPresets -> {
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    launch {
                        val reorderedColorPresets =
                            _state.value.colorPresets.toMutableList().apply {
                                add(event.to, removeAt(event.from))
                            }

                        _state.update {
                            it.copy(
                                colorPresets = reorderedColorPresets
                            )
                        }
                    }
                }
            }

            is SettingsEvent.OnConfirmReorderColorPresets -> {
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    launch {
                        reorderColorPresets.execute(_state.value.colorPresets)
                    }
                }
            }
        }
    }

    private fun cancelColorPresetJobs() {
        selectColorPresetJob?.cancel()
        addColorPresetJob?.cancel()
        updateTitleColorPresetJob?.cancel()
        shuffleColorPresetJob?.cancel()
        updateColorColorPresetJob?.cancel()
        deleteColorPresetJob?.cancel()
    }

    private suspend fun onSelectColorPreset(colorPreset: ColorPreset, animate: Boolean = false) {
        selectColorPreset.execute(colorPreset)
        _state.update {
            it.copy(
                animateColorPreset = animate
            )
        }
    }

    private fun getColorPresetById(id: ID): ColorPreset? {
        return _state.value.colorPresets.firstOrNull {
            it.id == id
        }
    }

    private fun selectedColorPreset(colorPresets: List<ColorPreset>? = null): ColorPreset {
        val presets = colorPresets ?: _state.value.colorPresets

        if (presets.size == 1) {
            return presets.first()
        }

        val selectedPreset = presets.firstOrNull { it.isSelected }

        if (selectedPreset == null) {
            return Constants.DEFAULT_COLOR_PRESET
        }

        return selectedPreset
    }

    private fun List<ColorPreset>.updateColorPreset(colorPreset: ColorPreset): List<ColorPreset> {
        if (size == 1) {
            return listOf(colorPreset)
        }

        return this.map {
            if (it.isSelected) {
                colorPreset
            } else {
                it
            }
        }
    }
}









