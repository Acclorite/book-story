package ua.acclorite.book_story.presentation.components.translator_language.components

import android.widget.Toast
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.custom_dialog.CustomDialogWithContent
import ua.acclorite.book_story.presentation.components.translator_language.data.TranslatorLanguageEvent
import ua.acclorite.book_story.presentation.components.translator_language.data.TranslatorLanguageState
import java.util.Locale

/**
 * Translator Language Download Dialog.
 * Used for downloading languages and selecting them right after.
 *
 * @param state [TranslatorLanguageState].
 * @param onSelect OnSelect callback. Happens when language is downloaded.
 * @param onEvent [TranslatorLanguageEvent] callback.
 */
@Composable
fun TranslatorLanguageDownloadDialog(
    state: State<TranslatorLanguageState>,
    onSelect: (String, String) -> Unit,
    onEvent: (TranslatorLanguageEvent) -> Unit
) {
    val context = LocalContext.current
    val displayLanguage = remember(state.value.languageToSelect) {
        val locale = Locale(state.value.languageToSelect)
        locale.getDisplayLanguage(locale).replaceFirstChar {
            it.uppercase()
        }
    }

    CustomDialogWithContent(
        title = stringResource(id = R.string.download_language, displayLanguage),
        imageVectorIcon = Icons.Default.Download,
        description = stringResource(
            id = R.string.download_language_description
        ),
        actionText = stringResource(id = R.string.download),
        isActionEnabled = true,
        disableOnClick = true,
        onDismiss = { onEvent(TranslatorLanguageEvent.OnDismissDownloadDialog) },
        onAction = {
            val selectedLanguage = state.value.languageToSelect

            onEvent(TranslatorLanguageEvent.OnDismissDownloadDialog)
            onEvent(
                TranslatorLanguageEvent.OnDownloadLanguage(
                    languageCode = state.value.languageToSelect,
                    onComplete = {
                        if (selectedLanguage == state.value.languageToSelect) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(
                                    context,
                                    context.getString(R.string.language_downloaded),
                                    Toast.LENGTH_LONG
                                ).show()
                            }

                            onEvent(
                                TranslatorLanguageEvent.OnSelectLanguage(
                                    languageCode = state.value.languageToSelect,
                                    onSelect = onSelect,
                                    onError = {
                                        Toast
                                            .makeText(
                                                context,
                                                it.asString(context),
                                                Toast.LENGTH_LONG
                                            )
                                            .show()
                                    }
                                )
                            )
                        }
                    },
                    onFailure = {
                        Toast.makeText(
                            context,
                            context.getString(
                                R.string.error_query,
                                it.message
                            ),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                )
            )
        },
        withDivider = false
    )
}