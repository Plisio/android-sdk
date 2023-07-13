package net.plisio.sdk.ui.compose.components

import androidx.annotation.DrawableRes
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.plisio.sdk.models.PlisioInvoice
import net.plisio.sdk.ui.compose.R

@Composable
fun PlisioInvoiceStatusIndicator(
    status: PlisioInvoice.Status,
    modifier: Modifier = Modifier,
    color: Color = LocalContentColor.current
) {
    when (status.indicator) {
        PlisioInvoice.Status.Indicator.Progress -> {
            CircularProgressIndicator(
                color = color,
                strokeWidth = 2.dp,
                modifier = modifier
            )
        }

        else -> {
            Icon(
                painter = painterResource(status.indicator.icon),
                tint = color,
                contentDescription = stringResource(status.description),
                modifier = modifier
            )
        }
    }
}

private val PlisioInvoice.Status.Indicator.icon: Int
    @DrawableRes get() = when (this) {
        PlisioInvoice.Status.Indicator.Progress -> R.drawable.plisio_icon_pending
        PlisioInvoice.Status.Indicator.Error -> R.drawable.plisio_icon_error
        PlisioInvoice.Status.Indicator.Completed -> R.drawable.plisio_icon_completed
    }