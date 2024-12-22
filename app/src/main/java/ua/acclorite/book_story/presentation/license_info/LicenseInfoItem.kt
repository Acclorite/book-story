package ua.acclorite.book_story.presentation.license_info

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mikepenz.aboutlibraries.entity.License
import ua.acclorite.book_story.ui.theme.SlidingTransition

@Composable
fun LazyItemScope.LicenseInfoItem(
    license: License
) {
    val showed = rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier.animateItem(
            fadeInSpec = null,
            fadeOutSpec = null
        )
    ) {
        Text(
            text = license.name,
            color = MaterialTheme.colorScheme.onPrimary,
            style = MaterialTheme.typography.titleSmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .clip(RoundedCornerShape(100))
                .clickable(enabled = license.licenseContent?.isNotBlank() == true) {
                    showed.value = !showed.value
                }
                .background(
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(100)
                )
                .padding(vertical = 6.dp, horizontal = 12.dp)
        )

        SlidingTransition(
            visible = showed.value && license.licenseContent?.isNotBlank() == true,
            modifier = Modifier.padding(horizontal = 8.dp)
        ) {
            Column {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = license.licenseContent!!
                        .lines()
                        .joinToString(separator = "\n") {
                            it.trim()
                        },
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}