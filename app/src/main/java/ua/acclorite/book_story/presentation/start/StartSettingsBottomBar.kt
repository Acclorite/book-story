package ua.acclorite.book_story.presentation.start

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R

@Composable
fun StartSettingsBottomBar(
    currentPage: Int,
    storagePermissionGranted: Boolean,
    navigateForward: () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(18.dp))
        Button(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 8.dp)
                .padding(horizontal = 18.dp)
                .fillMaxWidth(),
            onClick = { navigateForward() },
            enabled = storagePermissionGranted || currentPage != 2
        ) {
            Text(text = stringResource(id = R.string.next))
        }
    }
}