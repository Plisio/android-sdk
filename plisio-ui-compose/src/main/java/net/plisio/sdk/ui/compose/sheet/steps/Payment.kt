package net.plisio.sdk.ui.compose.sheet.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.plisio.sdk.models.PlisioAmount
import net.plisio.sdk.ui.compose.R
import net.plisio.sdk.ui.compose.components.LocalAdaptiveSheetLayoutState
import net.plisio.sdk.ui.compose.components.PlisioTextButton
import net.plisio.sdk.ui.compose.components.QRCodeImage
import net.plisio.sdk.ui.compose.copyToClipboard
import net.plisio.sdk.ui.compose.highlightedString
import net.plisio.sdk.ui.compose.style.LocalPlisioStyle
import net.plisio.sdk.ui.compose.style.PlisioTextStyle
import net.plisio.sdk.uimodels.PlisioPaymentStep

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun PlisioPaymentSheetPayment(
    paymentStep: PlisioPaymentStep.Payment,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        QRCodeImage(
            data = paymentStep.invoice.qrURL ?: paymentStep.invoice.walletHash,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .padding(horizontal = 16.dp)
                .heightIn(
                    min = 150.dp,
                    max = if (LocalAdaptiveSheetLayoutState.current.hasVerticalSpace) 300.dp else 150.dp
                )
        )
        Surface(
            color = LocalPlisioStyle.current.resolvedPaymentSheetBackground,
            onClick = copyToClipboard(R.string.plisio_payment_sheet_toast_amount_copied) { paymentStep.formattedUnpaidAmountPlain },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = when {
                    paymentStep.receivedAmount > PlisioAmount.Zero -> highlightedString(
                        R.string.plisio_payment_sheet_payment_text_partial,
                        paymentStep.formattedReceivedAmount,
                        paymentStep.formattedUnpaidAmount
                    )

                    else -> highlightedString(
                        R.string.plisio_payment_sheet_payment_text,
                        paymentStep.formattedUnpaidAmount
                    )
                },
                style = PlisioTextStyle.paymentSheetText,
                color = LocalContentColor.current.copy(alpha = 0.5f),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            )
        }
        Surface(
            color = LocalPlisioStyle.current.resolvedPaymentSheetBackground,
            onClick = copyToClipboard(R.string.plisio_payment_sheet_toast_wallet_copied) { paymentStep.invoice.walletHash },
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = paymentStep.invoice.walletHash,
                    style = PlisioTextStyle.paymentSheetWalletAddress
                )
                if (paymentStep.currency.contractStandard != null) {
                    Surface(
                        color = LocalPlisioStyle.current.primaryColor,
                        contentColor = LocalPlisioStyle.current.onPrimaryColor,
                        shape = CircleShape
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .padding(horizontal = 8.dp)
                                .heightIn(min = 28.dp)
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.plisio_icon_wallet),
                                contentDescription = null,
                                modifier = Modifier.size(12.dp)
                            )
                            Text(
                                text = paymentStep.currency.contractStandard.orEmpty(),
                                style = PlisioTextStyle.textButton,
                                fontSize = 14.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            PlisioTextButton(
                text = stringResource(R.string.plisio_payment_sheet_payment_action_copy_wallet),
                icon = painterResource(R.drawable.plisio_icon_copy),
                onClick = copyToClipboard(R.string.plisio_payment_sheet_toast_wallet_copied) { paymentStep.invoice.walletHash }
            )
            PlisioTextButton(
                text = stringResource(R.string.plisio_payment_sheet_payment_action_copy_amount),
                icon = painterResource(R.drawable.plisio_icon_copy),
                onClick = copyToClipboard(R.string.plisio_payment_sheet_toast_amount_copied) { paymentStep.formattedUnpaidAmountPlain }
            )
        }
    }
}