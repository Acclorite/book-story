@file:OptIn(ExperimentalMaterial3Api::class)

package ua.acclorite.book_story.presentation.components.translator_language.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.AnimatedTopAppBar
import ua.acclorite.book_story.presentation.components.CustomIconButton
import ua.acclorite.book_story.presentation.components.CustomSearchTextField
import ua.acclorite.book_story.presentation.components.translator_language.data.TranslatorLanguageEvent
import ua.acclorite.book_story.presentation.components.translator_language.data.TranslatorLanguageState

/**
 * Translator Language TopBar.
 *
 * @param state [TranslatorLanguageState].
 * @param scrollBehavior [TopAppBarScrollBehavior].
 * @param onDismissBottomSheet OnDismiss callback.
 * @param onEvent [TranslatorLanguageEvent] callback.
 */
@Composable
fun TranslatorLanguageTopBar(
    state: State<TranslatorLanguageState>,
    scrollBehavior: TopAppBarScrollBehavior,
    onDismissBottomSheet: () -> Unit,
    onEvent: (TranslatorLanguageEvent) -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    AnimatedTopAppBar(
        scrollBehavior = scrollBehavior,
        isTopBarScrolled = null,

        content1Visibility = !state.value.showSearch,
        content1NavigationIcon = {
            CustomIconButton(
                icon = Icons.Default.Close,
                contentDescription = R.string.go_back_content_desc,
                disableOnClick = true
            ) {
                onDismissBottomSheet()
            }
        },
        content1Title = {
            Text(
                text = stringResource(
                    id = R.string.translator_query, stringResource(
                        if (state.value.translateFromSelecting) R.string.translator_from
                        else R.string.translator_to
                    )
                )
            )
        },
        content1Actions = {
            CustomIconButton(
                icon = Icons.Default.Search,
                contentDescription = R.string.search_content_desc,
                disableOnClick = true
            ) {
                onEvent(TranslatorLanguageEvent.OnSearchShowHide)
            }
        },

        content2Visibility = state.value.showSearch,
        content2NavigationIcon = {
            CustomIconButton(
                icon = Icons.AutoMirrored.Default.ArrowBack,
                contentDescription = R.string.exit_search_content_desc,
                disableOnClick = true
            ) {
                onEvent(TranslatorLanguageEvent.OnSearchShowHide)
            }
        },
        content2Title = {
            CustomSearchTextField(
                modifier = Modifier
                    .focusRequester(focusRequester)
                    .onGloballyPositioned {
                        onEvent(TranslatorLanguageEvent.OnRequestFocus(focusRequester))
                    },
                query = state.value.searchQuery,
                onQueryChange = {
                    onEvent(TranslatorLanguageEvent.OnSearchQueryChange(it))
                },
                onSearch = { onEvent(TranslatorLanguageEvent.OnSearch) },
                placeholder = stringResource(
                    id = R.string.search_query,
                    stringResource(id = R.string.languages)
                )
            )
        }
    )
}