package ua.acclorite.book_story.presentation.core.components.progress_indicator

import androidx.compose.material3.CircularWavyProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun CircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 4.dp,
    gapSize: Dp = 2.dp
) {
    CircularWavyProgressIndicator(
        modifier = modifier,
        color = color,
        stroke = Stroke(
            width = with(LocalDensity.current) {
                strokeWidth.toPx()
            },
            cap = StrokeCap.Round
        ),
        trackStroke = Stroke(
            width = with(LocalDensity.current) {
                strokeWidth.toPx()
            },
            cap = StrokeCap.Round
        ),
        gapSize = gapSize
    )
}