/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.domain.model.reader.ColorPreset
import ua.acclorite.book_story.domain.use_case.category.AddCategoryUseCase
import ua.acclorite.book_story.domain.use_case.category.DeleteCategoryUseCase
import ua.acclorite.book_story.domain.use_case.category.GetCategoriesUseCase
import ua.acclorite.book_story.domain.use_case.category.UpdateCategoriesOrderUseCase
import ua.acclorite.book_story.domain.use_case.category.UpdateCategoryUseCase
import ua.acclorite.book_story.domain.use_case.color_preset.DeleteColorPresetUseCase
import ua.acclorite.book_story.domain.use_case.color_preset.GetColorPresetsUseCase
import ua.acclorite.book_story.domain.use_case.color_preset.ReorderColorPresetsUseCase
import ua.acclorite.book_story.domain.use_case.color_preset.SelectColorPresetUseCase
import ua.acclorite.book_story.domain.use_case.color_preset.UpdateColorPresetUseCase
import ua.acclorite.book_story.domain.use_case.permission.GrantPersistableUriPermissionUseCase
import ua.acclorite.book_story.domain.use_case.permission.ReleasePersistableUriPermissionUseCase
import ua.acclorite.book_story.domain.use_case.settings.UpdateLanguageUseCase
import javax.inject.Inject
import kotlin.coroutines.coroutineContext
import kotlin.random.Random

@HiltViewModel
class SettingsModel @Inject constructor(
    private val updateLanguageUseCase: UpdateLanguageUseCase,
    private val getColorPresetsUseCase: GetColorPresetsUseCase,
    private val updateColorPresetUseCase: UpdateColorPresetUseCase,
    private val selectColorPresetUseCase: SelectColorPresetUseCase,
    private val reorderColorPresetsUseCase: ReorderColorPresetsUseCase,
    private val deleteColorPresetUseCase: DeleteColorPresetUseCase,
    private val grantPersistableUriPermissionUseCase: GrantPersistableUriPermissionUseCase,
    private val releasePersistableUriPermissionUseCase: ReleasePersistableUriPermissionUseCase,
    private val addCategoryUseCase: AddCategoryUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val updateCategoryUseCase: UpdateCategoryUseCase,
    private val updateCategoriesOrderUseCase: UpdateCategoriesOrderUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase
) : ViewModel() {

    private val mutex = Mutex()

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _effects = MutableSharedFlow<SettingsEffect>()
    val effects = _effects.asSharedFlow()

    private val _initialized = MutableStateFlow(false)
    val initialized = _initialized.asStateFlow()

    private var colorPresetJob: Job? = null

    init {
        viewModelScope.launch {
            var colorPresets = getColorPresetsUseCase()

            if (colorPresets.isEmpty()) {
                updateColorPresetUseCase(ColorPreset.default)
                getColorPresetsUseCase().first().selectColorPreset()
                colorPresets = getColorPresetsUseCase()
            }

            val scrollIndex = colorPresets.indexOfFirst { it.isSelected }
            if (scrollIndex != -1) {
                _state.value.colorPresetListState.requestScrollToItem(
                    index = scrollIndex,
                    scrollOffset = 0
                )
            }

            _state.update {
                it.copy(
                    selectedColorPreset = colorPresets.getSelectedColorPreset(),
                    colorPresets = colorPresets,
                    categories = getCategoriesUseCase()
                )
            }

            _initialized.update { true }
        }
    }

    fun onEvent(event: SettingsEvent) {
        viewModelScope.launch {
            when (event) {
                is SettingsEvent.OnUpdateLanguage -> {
                    updateLanguageUseCase(event.language)
                }

                is SettingsEvent.OnGrantPersistableUriPermission -> {
                    grantPersistableUriPermissionUseCase(event.uri)
                }

                is SettingsEvent.OnReleasePersistableUriPermission -> {
                    releasePersistableUriPermissionUseCase(event.uri)
                }

                is SettingsEvent.OnCreateCategory -> {
                    addCategoryUseCase(event.title)
                    _state.update {
                        it.copy(
                            categories = getCategoriesUseCase()
                        )
                    }
                }

                is SettingsEvent.OnUpdateCategory -> {
                    updateCategoryUseCase(
                        category = event.category
                    )
                    _state.update {
                        it.copy(
                            categories = getCategoriesUseCase()
                        )
                    }
                }

                is SettingsEvent.OnUpdateCategoryOrder -> {
                    updateCategoriesOrderUseCase(
                        categories = event.categories
                    )
                    _state.update {
                        it.copy(
                            categories = getCategoriesUseCase()
                        )
                    }
                }

                is SettingsEvent.OnRemoveCategory -> {
                    deleteCategoryUseCase(
                        category = event.category
                    )
                    _state.update {
                        it.copy(
                            categories = getCategoriesUseCase()
                        )
                    }
                }

                is SettingsEvent.OnSelectColorPreset -> {
                    withContext(Dispatchers.IO) {
                        colorPresetJob?.join()
                        colorPresetJob = viewModelScope.launch(Dispatchers.Default) {
                            val colorPreset = _state.value.colorPresets.getColorPresetById(event.id)
                                ?: return@launch

                            ensureActive()

                            colorPreset.selectColorPreset(animate = true)
                            val colorPresets = _state.value.colorPresets.map {
                                it.copy(isSelected = colorPreset.id == it.id)
                            }
                            _state.update {
                                it.copy(
                                    selectedColorPreset = colorPresets.getSelectedColorPreset(),
                                    colorPresets = colorPresets
                                )
                            }
                        }
                    }
                }

                is SettingsEvent.OnSwitchColorPreset -> {
                    withContext(Dispatchers.IO) {
                        colorPresetJob?.join()
                        colorPresetJob = viewModelScope.launch(Dispatchers.Default) {
                            val colorPresets = _state.value.colorPresets
                            if (colorPresets.size == 1) return@launch

                            val selectedPreset = _state.value.selectedColorPreset
                            val selectedPresetIndex = colorPresets.indexOf(selectedPreset)
                            if (selectedPresetIndex == -1) return@launch

                            val newColorPresetIndex = if (event.previous) {
                                when (selectedPresetIndex) {
                                    0 -> colorPresets.lastIndex
                                    else -> selectedPresetIndex - 1
                                }
                            } else {
                                when (selectedPresetIndex) {
                                    colorPresets.lastIndex -> 0
                                    else -> selectedPresetIndex + 1
                                }
                            }
                            val newColorPreset = colorPresets.getOrNull(newColorPresetIndex)
                                ?: return@launch

                            ensureActive()

                            newColorPreset.selectColorPreset()
                            val updatedColorPresets = _state.value.colorPresets.map {
                                it.copy(isSelected = newColorPreset.id == it.id)
                            }
                            _state.update {
                                it.copy(
                                    selectedColorPreset = updatedColorPresets.getSelectedColorPreset(),
                                    colorPresets = updatedColorPresets
                                )
                            }

                            _effects.emit(SettingsEffect.OnSwitchedColorPreset(newColorPreset))
                        }
                    }
                }

                is SettingsEvent.OnDeleteColorPreset -> {
                    withContext(Dispatchers.IO) {
                        colorPresetJob?.join()
                        colorPresetJob = viewModelScope.launch(Dispatchers.Default) {
                            if (_state.value.colorPresets.size == 1) return@launch
                            val colorPreset = _state.value.colorPresets.getColorPresetById(event.id)
                                ?: return@launch

                            val position = _state.value.colorPresets.indexOf(colorPreset)
                            if (position == -1) return@launch

                            val nextPosition =
                                if (position == _state.value.colorPresets.lastIndex) {
                                    position - 1
                                } else {
                                    position
                                }

                            ensureActive()

                            deleteColorPresetUseCase(colorPreset)
                            val nextColorPreset = getColorPresetsUseCase().getOrNull(nextPosition)
                                ?: return@launch

                            nextColorPreset.selectColorPreset()
                            val colorPresets = getColorPresetsUseCase()

                            _state.update {
                                it.copy(
                                    selectedColorPreset = colorPresets.getSelectedColorPreset(),
                                    colorPresets = colorPresets
                                )
                            }

                            _state.value.colorPresetListState.requestScrollToItem(
                                index = nextPosition,
                                scrollOffset = 0
                            )
                        }
                    }
                }

                is SettingsEvent.OnUpdateColorPresetTitle -> {
                    withContext(Dispatchers.IO) {
                        colorPresetJob?.join()
                        colorPresetJob = viewModelScope.launch(Dispatchers.Default) {
                            val colorPreset = _state.value.colorPresets.getColorPresetById(event.id)
                                ?: return@launch
                            val updatedColorPreset = colorPreset.copy(name = event.title)

                            ensureActive()

                            updateColorPresetUseCase(updatedColorPreset)
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
                    withContext(Dispatchers.IO) {
                        colorPresetJob?.join()
                        colorPresetJob = viewModelScope.launch(Dispatchers.Default) {
                            val colorPreset = _state.value.colorPresets.getColorPresetById(event.id)
                                ?: return@launch
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

                            ensureActive()

                            updateColorPresetUseCase(shuffledColorPreset)
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
                    withContext(Dispatchers.IO) {
                        colorPresetJob?.join()
                        colorPresetJob = viewModelScope.launch(Dispatchers.Default) {
                            val newColorPreset = ColorPreset.default.copy(
                                backgroundColor = event.backgroundColor,
                                fontColor = event.fontColor
                            )

                            ensureActive()

                            updateColorPresetUseCase(newColorPreset)
                            getColorPresetsUseCase().last().selectColorPreset()
                            val colorPresets = getColorPresetsUseCase()

                            _state.update {
                                it.copy(
                                    selectedColorPreset = colorPresets.getSelectedColorPreset(),
                                    colorPresets = colorPresets
                                )
                            }

                            _state.value.colorPresetListState.requestScrollToItem(
                                index = colorPresets.lastIndex,
                                scrollOffset = 0
                            )
                        }
                    }
                }

                is SettingsEvent.OnUpdateColorPresetColor -> {
                    withContext(Dispatchers.IO) {
                        colorPresetJob?.join()
                        colorPresetJob = viewModelScope.launch(Dispatchers.Default) {
                            val colorPreset = _state.value.colorPresets.getColorPresetById(event.id)
                                ?: return@launch
                            val updatedColorPreset = colorPreset.copy(
                                backgroundColor = event.backgroundColor
                                    ?: colorPreset.backgroundColor,
                                fontColor = event.fontColor
                                    ?: colorPreset.fontColor
                            )

                            ensureActive()

                            updateColorPresetUseCase(updatedColorPreset)
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
                    withContext(Dispatchers.IO) {
                        colorPresetJob?.join()
                        withContext(Dispatchers.Default) {
                            val reorderedColorPresets = _state.value.colorPresets
                                .toMutableList()
                                .also { colorPresets ->
                                    colorPresets.add(event.to, colorPresets.removeAt(event.from))
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
                    withContext(Dispatchers.IO) {
                        colorPresetJob?.join()
                        withContext(Dispatchers.Default) {
                            reorderColorPresetsUseCase(_state.value.colorPresets)
                        }
                    }
                }
            }
        }
    }

    private suspend fun ColorPreset.selectColorPreset(animate: Boolean = false) {
        selectColorPresetUseCase(this)
        _state.update {
            it.copy(
                animateColorPreset = animate
            )
        }
    }

    private fun List<ColorPreset>.getColorPresetById(id: Int): ColorPreset? {
        return find {
            it.id == id
        }
    }

    private fun List<ColorPreset>.getSelectedColorPreset(): ColorPreset {
        if (size == 1) return first()
        find { it.isSelected }?.also { return it }
        return ColorPreset.default
    }

    private fun List<ColorPreset>.updateColorPreset(colorPreset: ColorPreset): List<ColorPreset> {
        if (size == 1) return listOf(colorPreset)
        return map {
            if (it.isSelected) colorPreset
            else it
        }
    }

    private suspend inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
        mutex.withLock {
            coroutineContext.ensureActive()
            this.value = function(this.value)
        }
    }
}