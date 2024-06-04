package ua.acclorite.book_story.presentation.screens.reader.components.download_language_dialog

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.customItems
import ua.acclorite.book_story.presentation.components.custom_dialog.CustomDialogWithLazyColumn
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderState

/**
 * Reader Download Language Dialog. Downloads multiple language models at the same time.
 *
 * @param state [ReaderState].
 * @param onEvent [ReaderEvent] callback.
 */
@Composable
fun ReaderDownloadLanguageDialog(
    state: State<ReaderState>,
    onEvent: (ReaderEvent) -> Unit
) {
    val context = LocalContext.current
    var isOnClickEnabled by remember { mutableStateOf(true) }
    val isDownloading by remember {
        derivedStateOf {
            state.value.languagesToTranslate.any { it.isDownloading }
        }
    }
    val isDownloaded by remember {
        derivedStateOf {
            state.value.languagesToTranslate.filter { it.isSelected }.none { !it.isDownloaded }
        }
    }
    val languages = remember(state.value.languagesToTranslate, isDownloading) {
        if (isDownloading) {
            state.value.languagesToTranslate.sortedByDescending { it.isDownloading }
        } else {
            state.value.languagesToTranslate
        }
    }

    CustomDialogWithLazyColumn(
        title = stringResource(id = R.string.download_languages),
        imageVectorIcon = Icons.Default.Download,
        description = stringResource(
            id = R.string.download_languages_description
        ),
        actionText = stringResource(
            if (!isDownloaded) R.string.download
            else R.string.continue_download
        ),
        disableOnClick = false,
        isActionEnabled = isOnClickEnabled,
        onDismiss = { onEvent(ReaderEvent.OnDismissDownloadLanguageDialog) },
        onAction = {
            isOnClickEnabled = false
            onEvent(
                ReaderEvent.OnDownloadLanguages(
                    onSuccess = {
                        Toast.makeText(
                            context,
                            context.getString(R.string.languages_downloaded),
                            Toast.LENGTH_LONG
                        ).show()

                        onEvent(
                            ReaderEvent.OnTranslateText(
                                it.keys,
                                error = { error ->
                                    Toast.makeText(
                                        context,
                                        error.asString(context),
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            )
                        )
                    },
                    error = {
                        Toast.makeText(
                            context,
                            it.asString(context),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            )
        },
        withDivider = true,
        items = {
            customItems(
                languages, key = { it.languageCode }
            ) {
                ReaderDownloadLanguageDialogItem(
                    language = it,
                    isEnabled = !isDownloading,
                    onClick = {
                        onEvent(
                            ReaderEvent.OnSelectLanguage(it)
                        )
                    }
                )
            }
        }
    )
}