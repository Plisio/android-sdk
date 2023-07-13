package net.plisio.sdk.ui.compose.sheet.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.plisio.sdk.models.PlisioInvoice
import net.plisio.sdk.ui.compose.LocalPlisioPaymentSheetDismissTrigger
import net.plisio.sdk.ui.compose.R
import net.plisio.sdk.ui.compose.components.PlisioButton
import net.plisio.sdk.ui.compose.highlightedString
import net.plisio.sdk.ui.compose.style.PlisioTextStyle
import net.plisio.sdk.uimodels.PlisioPaymentStep

@Composable
fun PlisioPaymentSheetCompletion(
    paymentStep: PlisioPaymentStep.Completion,
    modifier: Modifier = Modifier
) {
    val dismiss = LocalPlisioPaymentSheetDismissTrigger.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            painter = when (paymentStep.completion) {
                PlisioInvoice.Completion.Completed -> painterResource(R.drawable.plisio_icon_completed)
                PlisioInvoice.Completion.Overpaid -> painterResource(R.drawable.plisio_icon_invoice_overpaid)
                PlisioInvoice.Completion.Error -> painterResource(R.drawable.plisio_icon_error)
                else -> painterResource(R.drawable.plisio_icon_retry)
            },
            tint = when (paymentStep.completion) {
                PlisioInvoice.Completion.Error -> MaterialTheme.colors.error
                else -> MaterialTheme.colors.primary
            },
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(64.dp)
        )
        Text(
            text = when (paymentStep.completion) {
                PlisioInvoice.Completion.Completed -> stringResource(R.string.plisio_payment_sheet_completion_title)
                PlisioInvoice.Completion.PartiallyCompleted -> stringResource(R.string.plisio_payment_sheet_completion_title_partial)
                PlisioInvoice.Completion.Overpaid -> stringResource(R.string.plisio_payment_sheet_completion_title_overpaid)
                PlisioInvoice.Completion.Expired -> stringResource(R.string.plisio_payment_sheet_completion_title_expired)
                PlisioInvoice.Completion.Error -> stringResource(R.string.plisio_payment_sheet_completion_title_error)
            },
            style = PlisioTextStyle.paymentSheetTitle
        )
        Text(
            text = when (paymentStep.completion) {
                PlisioInvoice.Completion.Completed -> highlightedString(R.string.plisio_payment_sheet_completion_text)
                PlisioInvoice.Completion.PartiallyCompleted -> highlightedString(
                    R.string.plisio_payment_sheet_completion_text_partial,
                    paymentStep.formattedReceivedAmount,
                    paymentStep.formattedFullAmount
                )

                PlisioInvoice.Completion.Overpaid -> highlightedString(
                    R.string.plisio_payment_sheet_completion_text_overpaid,
                    paymentStep.formattedReceivedAmount
                )

                PlisioInvoice.Completion.Expired -> highlightedString(R.string.plisio_payment_sheet_completion_text_expired)
                PlisioInvoice.Completion.Error -> highlightedString(R.string.plisio_payment_sheet_completion_text_error)
            },
            style = PlisioTextStyle.paymentSheetText,
            color = LocalContentColor.current.copy(alpha = 0.5f)
        )
        PlisioButton(
            text = stringResource(R.string.plisio_payment_sheet_completion_action_dismiss),
            onClick = dismiss,
            modifier = Modifier.padding(top = 16.dp)
        )
    }
}