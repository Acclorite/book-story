package ua.acclorite.book_story.presentation.core.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.compositionLocalOf
import androidx.hilt.navigation.compose.hiltViewModel
import ua.acclorite.book_story.presentation.core.util.BaseViewModel
import ua.acclorite.book_story.presentation.data.MainEvent
import ua.acclorite.book_story.presentation.data.MainState
import ua.acclorite.book_story.presentation.data.MainViewModel
import ua.acclorite.book_story.presentation.screens.about.data.AboutEvent
import ua.acclorite.book_story.presentation.screens.about.data.AboutState
import ua.acclorite.book_story.presentation.screens.about.data.AboutViewModel
import ua.acclorite.book_story.presentation.screens.about.nested.license_info.data.LicenseInfoEvent
import ua.acclorite.book_story.presentation.screens.about.nested.license_info.data.LicenseInfoState
import ua.acclorite.book_story.presentation.screens.about.nested.license_info.data.LicenseInfoViewModel
import ua.acclorite.book_story.presentation.screens.about.nested.licenses.data.LicensesEvent
import ua.acclorite.book_story.presentation.screens.about.nested.licenses.data.LicensesState
import ua.acclorite.book_story.presentation.screens.about.nested.licenses.data.LicensesViewModel
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoEvent
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoState
import ua.acclorite.book_story.presentation.screens.book_info.data.BookInfoViewModel
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseEvent
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseState
import ua.acclorite.book_story.presentation.screens.browse.data.BrowseViewModel
import ua.acclorite.book_story.presentation.screens.help.data.HelpEvent
import ua.acclorite.book_story.presentation.screens.help.data.HelpState
import ua.acclorite.book_story.presentation.screens.help.data.HelpViewModel
import ua.acclorite.book_story.presentation.screens.history.data.HistoryEvent
import ua.acclorite.book_story.presentation.screens.history.data.HistoryState
import ua.acclorite.book_story.presentation.screens.history.data.HistoryViewModel
import ua.acclorite.book_story.presentation.screens.library.data.LibraryEvent
import ua.acclorite.book_story.presentation.screens.library.data.LibraryState
import ua.acclorite.book_story.presentation.screens.library.data.LibraryViewModel
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderEvent
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderState
import ua.acclorite.book_story.presentation.screens.reader.data.ReaderViewModel
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsEvent
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsState
import ua.acclorite.book_story.presentation.screens.settings.data.SettingsViewModel
import ua.acclorite.book_story.presentation.screens.start.data.StartEvent
import ua.acclorite.book_story.presentation.screens.start.data.StartState
import ua.acclorite.book_story.presentation.screens.start.data.StartViewModel

data class ViewModelComposition<S, E, V>(
    val state: State<S>,
    val onEvent: (E) -> Unit,
    val viewModel: V
)

@Composable
inline fun <reified V : BaseViewModel<S, E>, S, E> viewModelComposition(
): ProvidableCompositionLocal<ViewModelComposition<S, E, V>> {
    val viewModel: V = hiltViewModel()
    val state = viewModel.state.collectAsState()

    return compositionLocalOf {
        ViewModelComposition(
            state = state,
            onEvent = viewModel::onEvent,
            viewModel = viewModel
        )
    }
}

val LocalMainViewModel
    @Composable get() = viewModelComposition<MainViewModel, MainState, MainEvent>()
val LocalLibraryViewModel
    @Composable get() = viewModelComposition<LibraryViewModel, LibraryState, LibraryEvent>()
val LocalHistoryViewModel
    @Composable get() = viewModelComposition<HistoryViewModel, HistoryState, HistoryEvent>()
val LocalBrowseViewModel
    @Composable get() = viewModelComposition<BrowseViewModel, BrowseState, BrowseEvent>()
val LocalBookInfoViewModel
    @Composable get() = viewModelComposition<BookInfoViewModel, BookInfoState, BookInfoEvent>()
val LocalReaderViewModel
    @Composable get() = viewModelComposition<ReaderViewModel, ReaderState, ReaderEvent>()
val LocalSettingsViewModel
    @Composable get() = viewModelComposition<SettingsViewModel, SettingsState, SettingsEvent>()
val LocalAboutViewModel
    @Composable get() = viewModelComposition<AboutViewModel, AboutState, AboutEvent>()
val LocalLicensesViewModel
    @Composable get() = viewModelComposition<LicensesViewModel, LicensesState, LicensesEvent>()
val LocalLicenseInfoViewModel
    @Composable get() = viewModelComposition<LicenseInfoViewModel, LicenseInfoState, LicenseInfoEvent>()
val LocalHelpViewModel
    @Composable get() = viewModelComposition<HelpViewModel, HelpState, HelpEvent>()
val LocalStartViewModel
    @Composable get() = viewModelComposition<StartViewModel, StartState, StartEvent>()