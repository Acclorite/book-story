package ua.acclorite.book_story.presentation.screens.start.data

import androidx.compose.runtime.Immutable

@Immutable
data class StartState(
    val isDone: Boolean = false,

    val currentScreen: StartNavigationScreen = StartNavigationScreen.entries.first(),
    val useBackAnimation: Boolean = false,

    val storagePermissionGranted: Boolean = false,
    val notificationsPermissionGranted: Boolean = false,
)