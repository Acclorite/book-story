package ua.acclorite.book_story.presentation.components.translator_language

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CategoryTitle
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.CustomBottomSheet
import ua.acclorite.book_story.presentation.components.customItems
import ua.acclorite.book_story.presentation.components.is_messages.IsEmpty
import ua.acclorite.book_story.presentation.components.translator_language.components.TranslatorLanguageDownloadDialog
import ua.acclorite.book_story.presentation.components.translator_language.components.TranslatorLanguageItem
import ua.acclorite.book_story.presentation.components.translator_language.components.TranslatorLanguageItemWithIcon
import ua.acclorite.book_story.presentation.components.translator_language.components.TranslatorLanguageTopBar
import ua.acclorite.book_story.presentation.components.translator_language.data.TranslatorLanguageEvent
import ua.acclorite.book_story.presentation.components.translator_language.data.TranslatorLanguageState
import ua.acclorite.book_story.presentation.components.translator_language.data.TranslatorLanguageViewModel
import ua.acclorite.book_story.presentation.ui.DefaultTransition
import ua.acclorite.book_story.presentation.ui.Transitions

/**
 * Translator language bottom sheet.
 * Manages downloading, deleting and selecting language for translator.
 *
 * @param selectedLanguage Current selected language.
 * @param unselectedLanguage Current unselected language(second).
 * @param translateFromSelecting Whether user selects "Translate from" or "Translate to" language.
 * @param onDismiss Dismissing BottomSheet.
 * @param onSelect Gives two variables, first - Translate from, second - Translate to. If user selects "Translate from" and then selects current unselected language(in this case "Translate to"), then it also changes "Translate to" to previous "Translate from" value.
 */
@Composable
fun TranslatorLanguageBottomSheet(
    selectedLanguage: String,
    unselectedLanguage: String,
    translateFromSelecting: Boolean,
    onDismiss: () -> Unit,
    onSelect: (String, String) -> Unit
) {
    val translatorLanguageViewModel: TranslatorLanguageViewModel = hiltViewModel()

    val state = translatorLanguageViewModel.state.collectAsState()
    val loading = remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        translatorLanguageViewModel.init(
            selectedLanguage = selectedLanguage,
            unselectedLanguage = unselectedLanguage,
            translateFromSelecting = translateFromSelecting,
            loaded = {
                loading.value = false
            }
        )
    }

    TranslatorLanguageBottomSheetContent(
        state = state,
        loading = loading,
        onSelect = { from, to ->
            val fromSelecting = state.value.translateFromSelecting
            val languageToChoose = if (fromSelecting) from else to

            if (languageToChoose != "auto") {
                translatorLanguageViewModel.onEvent(
                    TranslatorLanguageEvent.OnUpdateLanguageHistory(
                        languageToChoose
                    )
                )
            }
            onSelect(from, to)
        },
        onDismiss = onDismiss,
        onEvent = translatorLanguageViewModel::onEvent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TranslatorLanguageBottomSheetContent(
    state: State<TranslatorLanguageState>,
    loading: State<Boolean>,
    onSelect: (String, String) -> Unit,
    onDismiss: () -> Unit,
    onEvent: (TranslatorLanguageEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val languagesWithHistory = remember(state.value.languages) {
        state.value.languages.filter {
            it.historyOrder != null
        }.sortedBy {
            it.historyOrder
        }
    }
    val languages = remember(state.value.languages) {
        state.value.languages.filter {
            it.historyOrder == null
        }.sortedBy {
            it.languageCode
        }
    }

    CustomBottomSheet(
        hasFixedHeight = true,
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        dragHandle = {},
        shape = RoundedCornerShape(0),
        scrimColor = MaterialTheme.colorScheme.surface,
        onDismissRequest = {
            onDismiss()
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(scrollBehavior.nestedScrollConnection),
            containerColor = Color.Transparent,
            topBar = {
                TranslatorLanguageTopBar(
                    state = state,
                    scrollBehavior = scrollBehavior,
                    onDismissBottomSheet = onDismiss,
                    onEvent = onEvent
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                DefaultTransition(
                    visible = !loading.value,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (state.value.showDownloadLanguageDialog) {
                        TranslatorLanguageDownloadDialog(
                            state = state,
                            onSelect = onSelect,
                            onEvent = onEvent
                        )
                    }

                    LazyColumn(Modifier.fillMaxSize()) {
                        item {
                            Spacer(
                                modifier = Modifier
                                    .animateItem()
                                    .height(12.dp)
                            )
                        }

                        if (
                            state.value.translateFromSelecting &&
                            (!state.value.showSearch || state.value.searchQuery.isEmpty())
                        ) {
                            item {
                                val isSelected = remember(state.value.selectedLanguage) {
                                    state.value.selectedLanguage == "auto"
                                }

                                TranslatorLanguageItemWithIcon(
                                    isSelected = isSelected,
                                    displayLanguage = stringResource(id = R.string.translator_auto),
                                    customIcon = Icons.Default.AutoAwesome,
                                    onSelect = {
                                        onSelect("auto", state.value.unselectedLanguage)
                                    }
                                )

                                Spacer(
                                    modifier = Modifier
                                        .animateItem()
                                        .height(36.dp)
                                )
                            }
                        }

                        if (languagesWithHistory.isNotEmpty()) {
                            item {
                                CategoryTitle(
                                    title = stringResource(id = R.string.translator_recent_languages),
                                    color = MaterialTheme.colorScheme.primary,
                                    padding = 28.dp,
                                    modifier = Modifier.animateItem()
                                )
                                Spacer(
                                    modifier = Modifier
                                        .animateItem()
                                        .height(14.dp)
                                )
                            }


                            customItems(
                                languagesWithHistory,
                                key = { it.languageCode }
                            ) { language ->
                                TranslatorLanguageItem(
                                    language = language,
                                    onSelect = onSelect,
                                    onEvent = onEvent
                                )
                            }
                        }

                        if (languages.isNotEmpty()) {
                            item {
                                if (languagesWithHistory.isNotEmpty()) {
                                    Spacer(
                                        modifier = Modifier
                                            .animateItem()
                                            .height(36.dp)
                                    )
                                }

                                CategoryTitle(
                                    title = stringResource(id = R.string.translator_all_languages),
                                    color = MaterialTheme.colorScheme.primary,
                                    padding = 28.dp,
                                    modifier = Modifier.animateItem()
                                )
                                Spacer(
                                    modifier = Modifier
                                        .animateItem()
                                        .height(14.dp)
                                )
                            }

                            customItems(
                                languages,
                                key = { it.languageCode }
                            ) { language ->
                                TranslatorLanguageItem(
                                    language = language,
                                    onSelect = onSelect,
                                    onEvent = onEvent
                                )
                            }
                        }

                        item {
                            Spacer(
                                modifier = Modifier
                                    .animateItem()
                                    .height(12.dp)
                            )
                        }
                    }

                    CustomAnimatedVisibility(
                        visible = state.value.languages.isEmpty(),
                        modifier = Modifier.align(Alignment.Center),
                        enter = Transitions.DefaultTransitionIn,
                        exit = fadeOut(tween(0))
                    ) {
                        IsEmpty(
                            message = stringResource(id = R.string.translator_languages_empty),
                            icon = painterResource(id = R.drawable.empty_languages)
                        )
                    }
                }
            }
        }
    }
}
