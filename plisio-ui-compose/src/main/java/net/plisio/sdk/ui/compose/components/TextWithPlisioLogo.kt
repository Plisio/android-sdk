package net.plisio.sdk.ui.compose.components

import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.plisio.sdk.ui.compose.R
import net.plisio.sdk.ui.compose.style.LocalPlisioStyle
import net.plisio.sdk.ui.compose.style.PlisioTextStyle

@Composable
fun TextWithPlisioLogo(
    modifier: Modifier = Modifier,
    text: String = stringResource(R.string.plisio_payment_button_text),
    color: Color = LocalContentColor.current,
    accentColor: Color = LocalPlisioStyle.current.primaryColor
) {
    TextWithInlineContent(
        text = text,
        contentText = "Plisio",
        color = color,
        style = PlisioTextStyle.paymentButton,
        fontSize = with(LocalDensity.current) { 18.dp.toSp() },
        modifier = modifier.padding(horizontal = 16.dp),
        textModifier = Modifier.absoluteOffset(y = (-0.5).dp)
    ) {
        PlisioLogo(
            color = color,
            accentColor = accentColor,
            contentDescription = it,
            modifier = Modifier.height(30.dp)
        )
    }
}