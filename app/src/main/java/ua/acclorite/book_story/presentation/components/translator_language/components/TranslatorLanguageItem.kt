package ua.acclorite.book_story.presentation.components.translator_language.components

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.Language
import ua.acclorite.book_story.presentation.components.translator_language.data.TranslatorLanguageEvent
import ua.acclorite.book_story.presentation.ui.DefaultTransition

/**
 * Translator Language Item with Icon.
 *
 * @param isSelected Whether item is selected.
 * @param displayLanguage Display language to be shown.
 * @param customIcon Icon.
 * @param onSelect OnSelect(click) callback.
 */
@Composable
fun LazyItemScope.TranslatorLanguageItemWithIcon(
    isSelected: Boolean,
    displayLanguage: String,
    customIcon: ImageVector,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .animateItem()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(100))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else Color.Transparent,
                RoundedCornerShape(100)
            )
            .clickable {
                onSelect()
            }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.selected_content_desc),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        Text(
            displayLanguage,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(16.dp))
        Icon(
            imageVector = customIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp)
        )
    }
}

/**
 * Translator Language Item with Icon.
 *
 * @param language [Language] item.
 * @param onSelect OnSelect(click) callback.
 * @param onEvent [TranslatorLanguageEvent] callback.
 */
@Composable
fun LazyItemScope.TranslatorLanguageItem(
    language: Language,
    onSelect: (String, String) -> Unit,
    onEvent: (TranslatorLanguageEvent) -> Unit
) {
    val context = LocalContext.current

    Row(
        modifier = Modifier
            .animateItem()
            .padding(horizontal = 12.dp)
            .clip(RoundedCornerShape(100))
            .background(
                if (language.isSelected) MaterialTheme.colorScheme.primaryContainer
                else Color.Transparent,
                RoundedCornerShape(100)
            )
            .clickable(enabled = !language.isDownloading) {
                onEvent(
                    TranslatorLanguageEvent.OnSelectLanguage(
                        languageCode = language.languageCode,
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
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (language.isSelected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = stringResource(R.string.selected_content_desc),
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }

        Text(
            language.displayLanguage,
            color = if (language.isSelected) MaterialTheme.colorScheme.onPrimaryContainer
            else MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f)
        )

        Box(contentAlignment = Alignment.CenterEnd) {
            DefaultTransition(!language.isDownloaded && !language.isDownloading) {
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Default.Download,
                        contentDescription = stringResource(id = R.string.download),
                        tint = if (language.isSelected) MaterialTheme.colorScheme.onSurfaceVariant
                        else MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = null,
                                indication = null
                            ) {
                                onEvent(
                                    TranslatorLanguageEvent.OnDownloadLanguage(
                                        languageCode = language.languageCode,
                                        onComplete = {},
                                        onFailure = {
                                            Toast
                                                .makeText(
                                                    context,
                                                    context.getString(
                                                        R.string.error_query,
                                                        it.message
                                                    ),
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    )
                                )
                            }
                    )
                }
            }

            DefaultTransition(language.isDownloading && !language.isDownloaded) {
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    CircularProgressIndicator(
                        strokeWidth = 2.7.dp,
                        modifier = Modifier
                            .size(22.dp)
                            .clickable(
                                interactionSource = null,
                                indication = null
                            ) {
                                onEvent(
                                    TranslatorLanguageEvent.OnCancelDownload(
                                        languageCode = language.languageCode
                                    )
                                )
                            },
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(1.dp))
                }
            }

            DefaultTransition(language.isDownloaded && !language.isDownloading) {
                Row {
                    Spacer(modifier = Modifier.width(16.dp))
                    Icon(
                        imageVector = Icons.Filled.CheckCircle,
                        contentDescription = stringResource(id = R.string.done),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(
                                interactionSource = null,
                                indication = null
                            ) {
                                onEvent(
                                    TranslatorLanguageEvent.OnDeleteLanguage(
                                        languageCode = language.languageCode
                                    )
                                )
                            }
                    )
                }
            }
        }
    }
}