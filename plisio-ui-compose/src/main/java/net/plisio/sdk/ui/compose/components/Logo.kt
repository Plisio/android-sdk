package net.plisio.sdk.ui.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import net.plisio.sdk.ui.compose.R
import net.plisio.sdk.ui.compose.style.LocalPlisioStyle

@Composable
fun PlisioLogo(
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current,
    accentColor: Color = LocalPlisioStyle.current.primaryColor,
    contentDescription: String? = "Plisio"
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(R.drawable.plisio_logo_text),
            tint = color,
            contentDescription = contentDescription
        )
        Icon(
            painter = painterResource(R.drawable.plisio_logo_accent),
            tint = accentColor,
            contentDescription = null
        )
    }
}