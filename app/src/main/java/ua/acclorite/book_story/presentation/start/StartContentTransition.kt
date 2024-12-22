package ua.acclorite.book_story.presentation.start

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ua.acclorite.book_story.domain.navigator.StackEvent
import ua.acclorite.book_story.ui.theme.Transitions

@Composable
fun StartContentTransition(
    modifier: Modifier = Modifier,
    targetValue: String,
    stackEvent: StackEvent,
    content: @Composable (String) -> Unit
) {
    AnimatedContent(
        modifier = Modifier
            .fillMaxSize()
            .then(modifier),
        targetState = targetValue,
        transitionSpec = {
            when (stackEvent) {
                StackEvent.Default -> {
                    Transitions.SlidingTransitionIn
                        .togetherWith(Transitions.SlidingTransitionOut)
                }

                StackEvent.Pop -> {
                    Transitions.BackSlidingTransitionIn
                        .togetherWith(Transitions.BackSlidingTransitionOut)
                }
            }
        },
        content = { content(it) }
    )
}