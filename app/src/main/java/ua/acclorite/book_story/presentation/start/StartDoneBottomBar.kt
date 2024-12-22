package ua.acclorite.book_story.presentation.start

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ua.acclorite.book_story.R

@Composable
fun StartDoneBottomBar(
    navigateToBrowse: () -> Unit,
    navigateToHelp: () -> Unit
) {
    Column {
        Spacer(modifier = Modifier.height(18.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .navigationBarsPadding()
                .padding(bottom = 8.dp)
                .padding(horizontal = 18.dp)
                .fillMaxWidth()
        ) {
            TextButton(
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(100),
                contentPadding = ButtonDefaults.ContentPadding,
                onClick = {
                    navigateToBrowse()
                }
            ) {
                Text(text = stringResource(id = R.string.no))
            }
            Spacer(modifier = Modifier.width(18.dp))
            Button(
                modifier = Modifier.weight(3f),
                shape = RoundedCornerShape(100),
                onClick = {
                    navigateToHelp()
                }
            ) {
                Text(text = stringResource(id = R.string.yes_go_to_help))
            }
        }
    }
}