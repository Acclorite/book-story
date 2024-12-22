package ua.acclorite.book_story.presentation.start

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun StartDoneScaffold(
    navigateToBrowse: () -> Unit,
    navigateToHelp: () -> Unit
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        bottomBar = {
            StartDoneBottomBar(
                navigateToBrowse = navigateToBrowse,
                navigateToHelp = navigateToHelp
            )
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) {
        StartDoneLayout(
            paddingValues = it
        )
    }
}