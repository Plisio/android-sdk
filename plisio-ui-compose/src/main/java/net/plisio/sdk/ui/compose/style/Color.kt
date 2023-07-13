package net.plisio.sdk.ui.compose.style

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.graphics.luminance

/** Plisio color scheme */
object PlisioColor {
    val primary = Color(0xFF4574EB)
    val secondary = Color(0xFF5C45EB)
    val light = Color(0xFFFFFFFF)
    val dark = Color(0xFF2B2D31)
    val darkBackground = Color(0xFF111111)
    val error = Color(0xFFEB4545)
}

/**
 * Returns contrasting content color for [background] color depending on its [luminance][Color.luminance]
 * @param background Background color
 * @param light Light content color
 * @param dark Dark content color
 * @param luminanceThreshold Threshold to consider [background] dark or light
 * @return Contrasting content color or [Color.Unspecified] if [background] is also [Color.Unspecified]
 */
fun contrastingColorFor(
    background: Color,
    light: Color = PlisioColor.light,
    dark: Color = PlisioColor.dark,
    luminanceThreshold: Float = 0.5f
): Color = when {
    background.isUnspecified -> Color.Unspecified
    background.luminance() > luminanceThreshold -> dark
    else -> light
}