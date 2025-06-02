/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2025 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.presentation.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import kotlinx.coroutines.yield
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.library.CategorySort
import ua.acclorite.book_story.domain.reader.ColorPreset
import ua.acclorite.book_story.domain.use_case.category.AddCategoryUseCase
import ua.acclorite.book_story.domain.use_case.category.DeleteCategoryUseCase
import ua.acclorite.book_story.domain.use_case.category.GetCategoriesUseCase
import ua.acclorite.book_story.domain.use_case.category.GetCategorySortingUseCase
import ua.acclorite.book_story.domain.use_case.category.UpdateCategoriesOrderUseCase
import ua.acclorite.book_story.domain.use_case.category.UpdateCategorySortingUseCase
import ua.acclorite.book_story.domain.use_case.category.UpdateCategoryUseCase
import ua.acclorite.book_story.domain.use_case.color_preset.DeleteColorPresetUseCase
import ua.acclorite.book_story.domain.use_case.color_preset.GetColorPresetsUseCase
import ua.acclorite.book_story.domain.use_case.color_preset.ReorderColorPresetsUseCase
import ua.acclorite.book_story.domain.use_case.color_preset.SelectColorPresetUseCase
import ua.acclorite.book_story.domain.use_case.color_preset.UpdateColorPresetUseCase
import ua.acclorite.book_story.domain.use_case.permission.GrantPersistableUriPermissionUseCase
import ua.acclorite.book_story.domain.use_case.permission.ReleasePersistableUriPermissionUseCase
import ua.acclorite.book_story.ui.common.constants.provideDefaultColorPreset
import ua.acclorite.book_story.ui.common.util.showToast
import javax.inject.Inject
import kotlin.random.Random

@HiltViewModel
class SettingsModel @Inject constructor(
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
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val updateCategorySortingUseCase: UpdateCategorySortingUseCase,
    private val getCategorySortingUseCase: GetCategorySortingUseCase
) : ViewModel() {

    private val mutex = Mutex()

    private val _state = MutableStateFlow(SettingsState())
    val state = _state.asStateFlow()

    private val _isReady = MutableStateFlow(false)
    val isReady = _isReady.asStateFlow()

    private var selectColorPresetJob: Job? = null
    private var addColorPresetJob: Job? = null
    private var deleteColorPresetJob: Job? = null
    private var updateColorColorPresetJob: Job? = null
    private var updateTitleColorPresetJob: Job? = null
    private var shuffleColorPresetJob: Job? = null

    init {
        viewModelScope.launch(Dispatchers.IO) {
            var colorPresets = getColorPresetsUseCase()

            if (colorPresets.isEmpty()) {
                updateColorPresetUseCase(provideDefaultColorPreset())
                getColorPresetsUseCase().first().select()
                colorPresets = getColorPresetsUseCase()
            }

            val scrollIndex = colorPresets.indexOfFirst {
                it.isSelected
            }
            if (scrollIndex != -1) {
                launch(Dispatchers.Main) {
                    try {
                        _state.value.colorPresetListState.requestScrollToItem(index = scrollIndex)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            _state.update {
                it.copy(
                    selectedColorPreset = colorPresets.selected(),
                    colorPresets = colorPresets,
                    categories = getCategoriesUseCase(), // Getting categories
                    categoriesSort = getCategorySortingUseCase()
                )
            }

            Log.i("SETTINGS", "SettingsModel is ready.")
            _isReady.update { true }
        }
    }

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnGrantPersistableUriPermission -> {
                viewModelScope.launch {
                    grantPersistableUriPermissionUseCase(
                        event.uri.toString()
                    )
                }
            }

            is SettingsEvent.OnReleasePersistableUriPermission -> {
                viewModelScope.launch {
                    releasePersistableUriPermissionUseCase(
                        event.uri.toString()
                    )
                }
            }

            is SettingsEvent.OnCreateCategory -> {
                viewModelScope.launch {
                    addCategoryUseCase(event.title)
                    _state.update {
                        it.copy(
                            categories = getCategoriesUseCase()
                        )
                    }
                }
            }

            is SettingsEvent.OnUpdateCategoryTitle -> {
                viewModelScope.launch {
                    updateCategoryUseCase(
                        categoryId = event.id,
                        newTitle = event.title
                    )
                    _state.update {
                        it.copy(
                            categories = getCategoriesUseCase()
                        )
                    }
                }
            }

            is SettingsEvent.OnUpdateCategoryOrder -> {
                viewModelScope.launch {
                    updateCategoriesOrderUseCase(
                        categories = event.categories
                    )
                    _state.update {
                        it.copy(
                            categories = getCategoriesUseCase()
                        )
                    }
                }
            }

            is SettingsEvent.OnRemoveCategory -> {
                viewModelScope.launch {
                    deleteCategoryUseCase(
                        category = event.category
                    )
                    _state.update {
                        it.copy(
                            categories = getCategoriesUseCase()
                        )
                    }
                }
            }

            is SettingsEvent.OnUpdateCategorySort -> {
                viewModelScope.launch {
                    updateCategorySortingUseCase(
                        categorySort = CategorySort(
                            categoryId = event.categoryId,
                            sortOrder = event.sortOrder,
                            sortOrderDescending = event.sortOrderDescending
                        )
                    )
                    _state.update {
                        it.copy(
                            categoriesSort = getCategorySortingUseCase()
                        )
                    }
                }
            }

            is SettingsEvent.OnSelectColorPreset -> {
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    selectColorPresetJob = launch {
                        val colorPreset = _state.value.colorPresets.getColorPresetById(event.id)
                            ?: return@launch

                        yield()

                        colorPreset.select(animate = true)
                        val colorPresets = _state.value.colorPresets.map {
                            it.copy(isSelected = colorPreset.id == it.id)
                        }
                        _state.update {
                            it.copy(
                                selectedColorPreset = colorPresets.selected(),
                                colorPresets = colorPresets
                            )
                        }
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

                        previousColorPreset.select()
                        val updatedColorPresets = _state.value.colorPresets.map {
                            it.copy(isSelected = previousColorPreset.id == it.id)
                        }
                        _state.update {
                            it.copy(
                                selectedColorPreset = updatedColorPresets.selected(),
                                colorPresets = updatedColorPresets
                            )
                        }

                        withContext(Dispatchers.Main) {
                            event.context.getString(
                                R.string.color_preset_selected_query,
                                if (previousColorPreset.name.isNullOrBlank()) {
                                    event.context.getString(
                                        R.string.color_preset_query,
                                        previousColorPreset.id.toString()
                                    )
                                } else {
                                    previousColorPreset.name
                                }.trim()
                            ).showToast(event.context, longToast = false)
                        }
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

                        nextColorPreset.select()
                        val updatedColorPresets = _state.value.colorPresets.map {
                            it.copy(isSelected = nextColorPreset.id == it.id)
                        }
                        _state.update {
                            it.copy(
                                selectedColorPreset = updatedColorPresets.selected(),
                                colorPresets = updatedColorPresets
                            )
                        }

                        withContext(Dispatchers.Main) {
                            event.context.getString(
                                R.string.color_preset_selected_query,
                                if (nextColorPreset.name.isNullOrBlank()) {
                                    event.context.getString(
                                        R.string.color_preset_query,
                                        nextColorPreset.id.toString()
                                    )
                                } else {
                                    nextColorPreset.name
                                }.trim()
                            ).showToast(event.context, longToast = false)
                        }
                    }
                }
            }

            is SettingsEvent.OnDeleteColorPreset -> {
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    deleteColorPresetJob = launch {
                        if (_state.value.colorPresets.size == 1) return@launch
                        val colorPreset = _state.value.colorPresets.getColorPresetById(event.id)
                            ?: return@launch

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

                        deleteColorPresetUseCase(colorPreset)
                        val nextColorPreset = getColorPresetsUseCase().getOrNull(nextPosition)
                            ?: return@launch

                        nextColorPreset.select()
                        val colorPresets = getColorPresetsUseCase()

                        _state.update {
                            it.copy(
                                selectedColorPreset = colorPresets.selected(),
                                colorPresets = colorPresets
                            )
                        }

                        onEvent(
                            SettingsEvent.OnScrollToColorPreset(
                                index = nextPosition
                            )
                        )
                    }
                }
            }

            is SettingsEvent.OnUpdateColorPresetTitle -> {
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    updateTitleColorPresetJob = launch {
                        val colorPreset = _state.value.colorPresets.getColorPresetById(event.id)
                            ?: return@launch

                        yield()

                        val updatedColorPreset = colorPreset.copy(
                            name = event.title
                        )

                        yield()

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
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    shuffleColorPresetJob = launch {
                        val colorPreset = _state.value.colorPresets.getColorPresetById(event.id)
                            ?: return@launch

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
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    addColorPresetJob = launch {
                        yield()

                        val newColorPreset = provideDefaultColorPreset().copy(
                            backgroundColor = event.backgroundColor,
                            fontColor = event.fontColor
                        )
                        updateColorPresetUseCase(newColorPreset)

                        getColorPresetsUseCase().last().select()
                        val colorPresets = getColorPresetsUseCase()

                        _state.update {
                            it.copy(
                                selectedColorPreset = colorPresets.selected(),
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
                        val colorPreset = _state.value.colorPresets.getColorPresetById(event.id)
                            ?: return@launch

                        yield()

                        val updatedColorPreset = colorPreset.copy(
                            backgroundColor = event.backgroundColor
                                ?: colorPreset.backgroundColor,
                            fontColor = event.fontColor
                                ?: colorPreset.fontColor
                        )

                        yield()

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
                viewModelScope.launch {
                    cancelColorPresetJobs()
                    launch {
                        val reorderedColorPresets = _state.value.colorPresets
                            .toMutableList()
                            .apply {
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
                        reorderColorPresetsUseCase(_state.value.colorPresets)
                    }
                }
            }

            is SettingsEvent.OnScrollToColorPreset -> {
                viewModelScope.launch {
                    try {
                        _state.value.colorPresetListState.requestScrollToItem(
                            index = event.index
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    private suspend fun ColorPreset.select(animate: Boolean = false) {
        selectColorPresetUseCase(this)
        _state.update {
            it.copy(
                animateColorPreset = animate
            )
        }
    }

    private fun List<ColorPreset>.getColorPresetById(id: Int): ColorPreset? {
        return firstOrNull {
            it.id == id
        }
    }

    private fun List<ColorPreset>?.selected(): ColorPreset {
        val presets = this ?: _state.value.colorPresets

        if (presets.size == 1) {
            return presets.first()
        }

        val selectedPreset = presets.firstOrNull { it.isSelected }

        if (selectedPreset == null) {
            return provideDefaultColorPreset()
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

    private fun cancelColorPresetJobs() {
        selectColorPresetJob?.cancel()
        addColorPresetJob?.cancel()
        updateTitleColorPresetJob?.cancel()
        shuffleColorPresetJob?.cancel()
        updateColorColorPresetJob?.cancel()
        deleteColorPresetJob?.cancel()
    }

    private suspend inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
        mutex.withLock {
            yield()
            this.value = function(this.value)
        }
    }
}