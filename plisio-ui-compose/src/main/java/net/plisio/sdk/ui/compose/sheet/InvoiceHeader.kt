package net.plisio.sdk.ui.compose.sheet

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.plisio.sdk.ui.compose.R
import net.plisio.sdk.ui.compose.components.LocalImageLoader
import net.plisio.sdk.ui.compose.copyToClipboard
import net.plisio.sdk.ui.compose.style.PlisioTextStyle
import net.plisio.sdk.uimodels.PlisioPaymentStep
import net.plisio.sdk.uimodels.PlisioPaymentStepWithInvoice

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun PlisioPaymentSheetInvoiceHeader(paymentStep: PlisioPaymentStep) {
    val invoiceStep = paymentStep as? PlisioPaymentStepWithInvoice

    Column(modifier = Modifier.fillMaxWidth()) {
        AnimatedVisibility(
            visible = invoiceStep != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            invoiceStep?.let { paymentStep ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp)
                        .padding(start = 16.dp)
                ) {
                    if (!invoiceStep.shop.logoURL.isNullOrEmpty()) {
                        Surface(
                            shape = CircleShape,
                            color = LocalContentColor.current.copy(alpha = 0.05f),
                            border = BorderStroke(1.dp, LocalContentColor.current.copy(alpha = 0.1f)),
                            content = {
                                AsyncImage(
                                    model = invoiceStep.shop.logoURL,
                                    modifier = Modifier.size(48.dp),
                                    contentDescription = null,
                                    imageLoader = LocalImageLoader.current
                                )
                            },
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .size(48.dp)
                        )
                    }
                    Text(
                        text = paymentStep.shop.name,
                        style = PlisioTextStyle.paymentSheetShopName,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Surface(
                        onClick = copyToClipboard(R.string.plisio_payment_sheet_toast_amount_copied) { paymentStep.formattedUnpaidAmountPlain },
                        color = Color.Transparent,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = paymentStep.formattedFullAmount,
                                style = PlisioTextStyle.paymentSheetAmount,
                                textAlign = TextAlign.End,
                                maxLines = 2,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    if (paymentStep is PlisioPaymentStep.Payment) {
        BackHandler(
            enabled = paymentStep.canChangeCurrency,
            onBack = paymentStep::changeCurrency
        )
    }
}