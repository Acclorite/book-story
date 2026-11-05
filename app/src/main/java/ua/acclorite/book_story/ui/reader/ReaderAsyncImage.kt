/*
 * Book's Story — free and open-source Material You eBook reader.
 * Copyright (C) 2024-2026 Acclorite
 * SPDX-License-Identifier: GPL-3.0-only
 */

package ua.acclorite.book_story.ui.reader

import android.graphics.drawable.Animatable
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.request.ImageRequest

@Composable
fun ReaderAsyncImage(
    content: String,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
    colorFilter: ColorFilter? = null,
    animationEnabled: Boolean = true,
    crossfadeMillis: Int = 0
) {
    val context = LocalContext.current
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val request = remember(context, content, crossfadeMillis) {
        ImageRequest.Builder(context)
            .data(content)
            .crossfade(crossfadeMillis)
            .build()
    }
    var animation by remember(content) { mutableStateOf<Animatable?>(null) }
    var visible by remember(content) { mutableStateOf(false) }
    var resumed by remember(lifecycle) {
        mutableStateOf(lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED))
    }

    DisposableEffect(lifecycle) {
        val observer = LifecycleEventObserver { _, _ ->
            resumed = lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)
        }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    val shouldPlay = animationEnabled && visible && resumed
    DisposableEffect(animation, shouldPlay) {
        val drawable = animation
        if (shouldPlay) {
            if (drawable?.isRunning == false) drawable.start()
        } else {
            drawable?.stop()
        }
        onDispose { drawable?.stop() }
    }

    AsyncImage(
        model = request,
        contentDescription = null,
        modifier = modifier.onGloballyPositioned { coordinates ->
            val bounds = coordinates.boundsInWindow()
            visible = bounds.width > 0f && bounds.height > 0f
        },
        onState = { state ->
            val drawable = (state as? AsyncImagePainter.State.Success)?.result?.drawable as? Animatable
            if (!shouldPlay) drawable?.stop()
            animation = drawable
        },
        contentScale = contentScale,
        colorFilter = colorFilter
    )
}
