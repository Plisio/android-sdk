package net.plisio.sdk.ui.compose.sheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import net.plisio.sdk.models.PlisioInvoice
import net.plisio.sdk.ui.compose.components.PlisioInvoiceStatusIndicator
import net.plisio.sdk.ui.compose.style.PlisioColor
import net.plisio.sdk.ui.compose.style.PlisioTextStyle
import net.plisio.sdk.uimodels.PlisioPaymentStep
import net.plisio.sdk.uimodels.PlisioPaymentStepWithInvoice
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

@Composable
fun PlisioPaymentSheetCountdown(
    start: Instant?,
    end: Instant?,
    status: PlisioInvoice.Status,
    modifier: Modifier = Modifier,
    color: Color = PlisioColor.dark.copy(alpha = 0.1f),
    progressColor: Color = color.copy(alpha = 0.2f),
    contentColor: Color = LocalContentColor.current
) {
    var now by remember { mutableStateOf(Clock.System.now()) }
    val nowToEnd = remember(now, end) { (end ?: now) - now }
    val countdownText = remember(nowToEnd) { nowToEnd.toCountdownText() }

    val progress = if (end != null && status.indicator == PlisioInvoice.Status.Indicator.Progress) {
        val startOrNow = remember(start) { start ?: Clock.System.now() }
        val range = remember(startOrNow, end) { end - startOrNow }
        val startToNow = remember(startOrNow, now) { now - startOrNow }
        (startToNow / range).toFloat().coerceIn(0f, 1f)
    } else 0f

    Surface(
        color = color,
        contentColor = contentColor,
        modifier = modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 24.dp)
            .height(IntrinsicSize.Min)
    ) {
        Box(
            contentAlignment = Alignment.CenterStart,
            modifier = Modifier.fillMaxSize()
        ) {
            Surface(
                color = progressColor,
                content = {},
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            PlisioInvoiceStatusIndicator(
                status = status,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(status.description),
                style = PlisioTextStyle.paymentSheetCountdown,
                maxLines = 1,
                modifier = Modifier.weight(1f)
            )
            if (status.indicator == PlisioInvoice.Status.Indicator.Progress && end != null && now <= end) {
                Text(
                    text = countdownText,
                    style = PlisioTextStyle.paymentSheetCountdown,
                    maxLines = 1
                )
            }
        }
    }

    LaunchedEffect(start, end) {
        while (isActive && end != null && now <= end) {
            delay(1.seconds)
            now = Clock.System.now()
        }
    }
}

@Composable
fun PlisioPaymentSheetCountdown(
    invoice: PlisioInvoice,
    modifier: Modifier = Modifier,
    color: Color = PlisioColor.dark.copy(alpha = 0.1f),
    progressColor: Color = color.copy(alpha = 0.2f),
    contentColor: Color = LocalContentColor.current
) {
    PlisioPaymentSheetCountdown(
        start = invoice.creationDate?.toInstant(),
        end = invoice.expirationDate?.toInstant(),
        status = invoice.status,
        modifier = modifier,
        color = color,
        progressColor = progressColor,
        contentColor = contentColor
    )
}

@Composable
fun PlisioPaymentSheetCountdown(
    paymentStep: PlisioPaymentStep,
    modifier: Modifier = Modifier,
    color: Color = PlisioColor.dark.copy(alpha = 0.1f),
    progressColor: Color = color.copy(alpha = 0.2f),
    contentColor: Color = LocalContentColor.current
) {
    val invoiceStep = paymentStep as? PlisioPaymentStepWithInvoice
    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = invoiceStep != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            invoiceStep?.invoice?.let { invoice ->
                PlisioPaymentSheetCountdown(
                    invoice = invoice,
                    modifier = modifier,
                    color = color,
                    progressColor = progressColor,
                    contentColor = contentColor
                )
            }
        }
    }
}

private fun Duration.toCountdownText(separator: Char = ':', daysSuffix: String = "d ") =
    toComponents { days, hours, minutes, seconds, _ ->
        buildString {
            if (days > 0) {
                append(days)
                append(daysSuffix)
            }
            if (hours > 0) {
                if (hours < 10) append('0')
                append(hours)
                append(separator)
            }
            if (minutes < 10) append('0')
            append(minutes)
            append(separator)
            if (seconds < 10) append('0')
            append(seconds)
        }
    }