package net.plisio.sdk.ui.compose.components

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalContext
import coil.ImageLoader
import coil.decode.SvgDecoder

@Composable
internal fun rememberImageLoader(context: Context = LocalContext.current) = remember {
    ImageLoader.Builder(context)
        .components {
            add(SvgDecoder.Factory())
        }
        .crossfade(150)
        .build()
}

internal val LocalImageLoader = staticCompositionLocalOf<ImageLoader> { error("LocalImageLoader was not provided as CompositionLocal") }