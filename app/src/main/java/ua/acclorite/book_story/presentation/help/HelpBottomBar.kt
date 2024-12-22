package ua.acclorite.book_story.presentation.help

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
import ua.acclorite.book_story.ui.browse.BrowseScreen
import ua.acclorite.book_story.ui.main.MainEvent

@Composable
fun HelpBottomBar(
    changeShowStartScreen: (MainEvent.OnChangeShowStartScreen) -> Unit,
    navigateToBrowse: () -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.height(18.dp))
        Button(
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 8.dp)
                .padding(horizontal = 18.dp)
                .fillMaxWidth(),
            onClick = {
                BrowseScreen.refreshListChannel.trySend(Unit)
                changeShowStartScreen(MainEvent.OnChangeShowStartScreen(false))
                navigateToBrowse()
            }
        ) {
            Text(text = stringResource(id = R.string.done))
        }
    }
}