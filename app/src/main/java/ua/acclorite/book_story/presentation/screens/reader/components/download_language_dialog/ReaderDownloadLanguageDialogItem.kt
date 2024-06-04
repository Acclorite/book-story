package ua.acclorite.book_story.presentation.screens.reader.components.download_language_dialog

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DownloadDone
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R
import ua.acclorite.book_story.domain.model.SelectableLanguage
import ua.acclorite.book_story.presentation.components.CustomCheckbox
import ua.acclorite.book_story.presentation.ui.DefaultTransition

/**
 * Reader Download Language Dialog Item.
 *
 * @param language [SelectableLanguage] item.
 * @param isEnabled Whether OnClick is enabled or not.
 * @param onClick OnClick callback.
 */
@Composable
fun LazyItemScope.ReaderDownloadLanguageDialogItem(
    language: SelectableLanguage,
    isEnabled: Boolean = true,
    onClick: () -> Unit
) {
    val context = LocalContext.current

    if (
        isEnabled || language.isDownloading || language.isDownloaded || language.isError
    ) {
        Row(
            modifier = Modifier
                .animateItem(
                    fadeInSpec = null,
                    fadeOutSpec = null
                )
                .fillMaxWidth()
                .clickable(
                    enabled = !language.isDownloading &&
                            !language.isDownloaded &&
                            language.canUnselect &&
                            isEnabled || language.isError
                ) {
                    if (!language.isError) {
                        onClick()
                    } else if (language.errorMessage != null) {
                        Toast
                            .makeText(
                                context,
                                language.errorMessage.asString(context),
                                Toast.LENGTH_LONG
                            )
                            .show()
                    }
                }
                .padding(vertical = 12.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(Modifier.weight(1f)) {
                Text(
                    text = language.displayLanguage,
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!language.occurrences.isNullOrEmpty()) {
                    Text(
                        text = stringResource(
                            id = R.string.translator_occurrences,
                            language.occurrences.size
                        ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }


            Box(contentAlignment = Alignment.CenterEnd) {
                DefaultTransition(
                    language.isDownloading && !language.isDownloaded && !language.isError
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(24.dp))
                        CircularProgressIndicator(
                            strokeWidth = 2.5.dp,
                            modifier = Modifier
                                .size(22.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                }


                DefaultTransition(
                    language.isDownloaded && !language.isDownloading && !language.isError
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(24.dp))
                        Icon(
                            imageVector = Icons.Default.DownloadDone,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    MaterialTheme.colorScheme.outlineVariant,
                                    shape = CircleShape
                                )
                                .padding(6.dp)
                                .size(17.dp),
                            contentDescription = stringResource(id = R.string.downloaded_content_desc)
                        )
                    }
                }

                DefaultTransition(
                    !language.isDownloaded && !language.isDownloading && isEnabled && !language.isError
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(24.dp))
                        CustomCheckbox(
                            selected = language.isSelected,
                            size = 20.dp,
                            containerColor =
                            if (language.canUnselect) MaterialTheme.colorScheme.surfaceContainerHigh
                            else MaterialTheme.colorScheme.surfaceContainerHigh
                        )
                    }
                }

                DefaultTransition(
                    !language.isDownloaded && !language.isDownloading && language.isError
                ) {
                    Row {
                        Spacer(modifier = Modifier.width(24.dp))
                        Icon(
                            imageVector = Icons.Default.Error,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier
                                .size(27.dp),
                            contentDescription = stringResource(id = R.string.error_content_desc)
                        )
                        Spacer(modifier = Modifier.width(1.5.dp))
                    }
                }
            }
        }
    }

}