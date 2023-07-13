package net.plisio.sdk.ui.compose.sheet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.plisio.sdk.ui.compose.LocalPlisioPaymentSheetDismissTrigger
import net.plisio.sdk.ui.compose.R
import net.plisio.sdk.ui.compose.components.LocalAdaptiveSheetLayoutState
import net.plisio.sdk.ui.compose.components.PlisioLogo
import net.plisio.sdk.ui.compose.style.LocalPlisioStyle
import net.plisio.sdk.ui.compose.style.PlisioStyle
import net.plisio.sdk.uimodels.PlisioPaymentStep

@Composable
fun PlisioPaymentSheetHeader(paymentStep: PlisioPaymentStep) {
    val style = LocalPlisioStyle.current
    val colors = when (val headerStyle = style.paymentSheetHeader) {
        is PlisioStyle.PaymentSheetHeader.Colored -> HeaderColors(
            background = headerStyle.color.takeOrElse { style.resolvedPaymentSheetBackground },
            foreground = headerStyle.contentColor.takeOrElse { style.resolvedPaymentSheetContent },
            accent = headerStyle.contentColor.takeOrElse { style.primaryColor }
        )

        is PlisioStyle.PaymentSheetHeader.Gradient -> HeaderColors(
            background = headerStyle.start,
            foreground = headerStyle.contentColor,
            modifier = Modifier.background(
                brush = Brush.linearGradient(
                    colors = listOf(headerStyle.start, headerStyle.end),
                    start = headerStyle.startOffset,
                    end = headerStyle.endOffset
                )
            )
        )
    }

    val statusBarHeight = WindowInsets.statusBars.getTop(LocalDensity.current)
    val sheetOffset: Int? = LocalAdaptiveSheetLayoutState.current.sheetOffset?.toInt()

    val topPadding = when {
        sheetOffset == null || sheetOffset > statusBarHeight -> 0.dp
        else -> with(LocalDensity.current) {
            (statusBarHeight - sheetOffset).coerceAtLeast(0).toDp()
        }
    }

    Surface(
        color = colors.background,
        contentColor = colors.foreground,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = colors.modifier.padding(top = topPadding)
        ) {
            AnimatedVisibility(visible = style.showPaymentSheetHeader) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    PlisioLogo(
                        color = colors.foreground,
                        accentColor = colors.accent,
                        modifier = Modifier
                            .height(32.dp)
                            .padding(horizontal = 16.dp)
                    )
                    IconButton(
                        onClick = LocalPlisioPaymentSheetDismissTrigger.current,
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plisio_icon_close),
                            tint = colors.foreground,
                            contentDescription = stringResource(R.string.plisio_payment_sheet_action_close),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
            PlisioPaymentSheetCountdown(
                paymentStep = paymentStep,
                color = colors.foreground.copy(alpha = 0.1f)
            )
        }
    }
}

private data class HeaderColors(
    val background: Color,
    val foreground: Color,
    val accent: Color = foreground,
    val modifier: Modifier = Modifier
)