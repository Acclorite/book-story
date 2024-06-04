package ua.acclorite.book_story.presentation.screens.reader.components.translator_bottom_sheet

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.CompareArrows
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import ua.acclorite.book_story.R
import ua.acclorite.book_story.presentation.components.CustomAnimatedVisibility
import ua.acclorite.book_story.presentation.components.CustomIconButton

/**
 * Reader Translator BottomSheet Language Chooser.
 *
 * @param fromLanguage Current translate from language.
 * @param toLanguage Current translate to language.
 * @param enabled Whether it is enabled.
 * @param switchEnabled Whether switch languages button is enabled.
 * @param horizontalPadding Horizontal item padding.
 * @param onFromClick OnFromClick callback.
 * @param onToClick OnToClick callback.
 * @param onSwitchClick OnSwitchClick callback.
 */
@Composable
fun ReaderTranslatorBottomSheetLanguageChooser(
    fromLanguage: String,
    toLanguage: String,
    enabled: Boolean = true,
    switchEnabled: Boolean = true,
    horizontalPadding: Dp = 18.dp,
    onFromClick: () -> Unit,
    onToClick: () -> Unit,
    onSwitchClick: () -> Unit
) {
    val switchLanguage = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var job: Job? = null

    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.weight(1f)) {
            CustomLanguageSwitchingAnimation(
                visible = switchLanguage.value,
                rightPlacement = false
            ) {
                LanguageItem(text = fromLanguage, enabled = enabled) {
                    onFromClick()
                }
            }

            CustomLanguageSwitchingAnimation(
                visible = !switchLanguage.value,
                rightPlacement = false
            ) {
                LanguageItem(text = fromLanguage, enabled = enabled) {
                    onFromClick()
                }
            }
        }



        Spacer(modifier = Modifier.width(4.dp))
        CustomIconButton(
            icon = Icons.AutoMirrored.Filled.CompareArrows,
            contentDescription = R.string.translator_language_content_desc,
            disableOnClick = false,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.size(24.dp),
            enabled = enabled && switchEnabled,
            onClick = {
                job?.cancel()
                job = scope.launch {
                    switchLanguage.value = !switchLanguage.value

                    delay(100)
                    yield()

                    onSwitchClick()
                }
            }
        )
        Spacer(modifier = Modifier.width(4.dp))

        Box(modifier = Modifier.weight(1f)) {
            CustomLanguageSwitchingAnimation(
                visible = switchLanguage.value,
                rightPlacement = true
            ) {
                LanguageItem(text = toLanguage, enabled = enabled) {
                    onToClick()
                }
            }

            CustomLanguageSwitchingAnimation(
                visible = !switchLanguage.value,
                rightPlacement = true
            ) {
                LanguageItem(text = toLanguage, enabled = enabled) {
                    onToClick()
                }
            }
        }
    }
}

@Composable
private fun CustomLanguageSwitchingAnimation(
    visible: Boolean,
    rightPlacement: Boolean,
    content: @Composable () -> Unit
) {
    val exitRightAnimation = remember {
        fadeOut(tween(200)) +
                slideOutHorizontally(tween(250)) { -it / 2 } +
                scaleOut(tween(250), 0.4f)
    }
    val enterRightAnimation = remember {
        slideInHorizontally(tween(350)) { -it / 2 } + fadeIn(tween(200))
    }

    val exitLeftAnimation = remember {
        fadeOut(tween(200)) +
                slideOutHorizontally(tween(250)) { it / 2 } +
                scaleOut(tween(250), 0.4f)
    }
    val enterLeftAnimation = remember {
        slideInHorizontally(tween(350)) { it / 2 } + fadeIn(tween(200))
    }

    CustomAnimatedVisibility(
        visible = visible,
        enter = if (rightPlacement) enterRightAnimation else enterLeftAnimation,
        exit = if (rightPlacement) exitRightAnimation else exitLeftAnimation
    ) {
        content()
    }
}

@Composable
private fun LanguageItem(text: String, enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.shapes.large)
            .clickable(enabled = enabled) {
                onClick()
            }
            .padding(horizontal = 4.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )
    }
}