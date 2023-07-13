package net.plisio.sdk.ui.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import net.plisio.sdk.ui.compose.components.PlisioInvoiceStatusIndicator
import net.plisio.sdk.ui.compose.components.TextWithPlisioLogo
import net.plisio.sdk.ui.compose.style.LocalPlisioStyle
import net.plisio.sdk.ui.compose.style.PlisioStyle
import net.plisio.sdk.uimodels.PlisioPaymentStep
import net.plisio.sdk.uimodels.PlisioPaymentStepWithInvoice

@Composable
fun PlisioPaymentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    paymentStep: PlisioPaymentStep = PlisioPaymentStep.Default,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = null,
    shape: Shape = MaterialTheme.shapes.small,
    indicatorShape: Shape = shape,
    style: PlisioStyle.PaymentButton = LocalPlisioStyle.current.paymentButton,
    backgroundColor: Color = LocalPlisioStyle.current.resolvedPaymentButtonBackground,
    contentColor: Color = LocalPlisioStyle.current.resolvedPaymentButtonContent,
    accentColor: Color = LocalPlisioStyle.current.primaryColor,
    onAccentColor: Color = LocalPlisioStyle.current.onPrimaryColor,
    border: BorderStroke? = BorderStroke(2.dp, accentColor),
    content: @Composable BoxScope.() -> Unit = { TextWithPlisioLogo() }
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        elevation = elevation,
        border = null,
        shape = shape,
        colors = ButtonDefaults.outlinedButtonColors(
            backgroundColor = accentColor,
            contentColor = contentColor,
            disabledContentColor = contentColor.copy(alpha = ContentAlpha.disabled)
        ),
        contentPadding = PaddingValues(0.dp)
    ) {
        Column(verticalArrangement = Arrangement.Center) {
            Surface(
                color = backgroundColor,
                shape = shape,
                border = border,
                modifier = Modifier.defaultMinSize(minHeight = 48.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        content()
                    }
                    AnimatedVisibility(
                        visible = paymentStep is PlisioPaymentStepWithInvoice,
                        enter = fadeIn() + expandHorizontally(
                            clip = false,
                            expandFrom = Alignment.Start
                        ),
                        exit = fadeOut() + shrinkHorizontally(
                            clip = false,
                            shrinkTowards = Alignment.Start
                        )
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .padding(end = 6.dp)
                                .size(36.dp)
                                .background(accentColor.copy(alpha = 0.2f), shape = indicatorShape)
                        ) {
                            (paymentStep as? PlisioPaymentStepWithInvoice)?.invoice?.status?.let { status ->
                                PlisioInvoiceStatusIndicator(
                                    status = status,
                                    color = accentColor,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }
            when (style) {
                PlisioStyle.PaymentButton.Default -> {}
                PlisioStyle.PaymentButton.Icons -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(R.drawable.plisio_currencies),
                            contentDescription = null,
                            modifier = Modifier.padding(
                                start = 8.dp,
                                end = 8.dp,
                                top = 6.dp - (border?.width ?: 0.dp),
                                bottom = 6.dp
                            )
                        )
                    }
                }

                PlisioStyle.PaymentButton.FlatIcons -> {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.plisio_currencies_flat),
                            contentDescription = null,
                            tint = onAccentColor,
                            modifier = Modifier.padding(
                                start = 8.dp,
                                end = 8.dp,
                                top = 8.dp - (border?.width ?: 0.dp),
                                bottom = 8.dp
                            )
                        )
                    }
                }
            }
        }
    }
}