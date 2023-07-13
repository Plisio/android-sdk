package net.plisio.sdk.ui.compose.sheet

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import net.plisio.sdk.ui.compose.R
import net.plisio.sdk.ui.compose.sheet.steps.PlisioPaymentSheetCompletion
import net.plisio.sdk.ui.compose.sheet.steps.PlisioPaymentSheetConfirmation
import net.plisio.sdk.ui.compose.sheet.steps.PlisioPaymentSheetCurrency
import net.plisio.sdk.ui.compose.sheet.steps.PlisioPaymentSheetError
import net.plisio.sdk.ui.compose.sheet.steps.PlisioPaymentSheetPayment
import net.plisio.sdk.ui.compose.sheet.steps.PlisioPaymentSheetUserEmail
import net.plisio.sdk.uimodels.PlisioPaymentStep

@Composable
fun PlisioPaymentSheetContent(paymentStep: PlisioPaymentStep) {
    val isLoading = paymentStep is PlisioPaymentStep.Loading || (paymentStep as? PlisioPaymentStep.Initial)?.isLoading == true

    Column(modifier = Modifier.fillMaxSize()) {
        PlisioPaymentSheetInvoiceHeader(paymentStep)
        Spacer(Modifier.weight(1f))
        Crossfade(isLoading, label = "PlisioPaymentSheetContent") { isLoading ->
            if (isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize(),
                    content = { CircularProgressIndicator() }
                )
            } else {
                when (paymentStep) {
                    is PlisioPaymentStep.Initial -> {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Text(
                                text = stringResource(R.string.plisio_payment_sheet_action_initial_no_invoice),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    is PlisioPaymentStep.UserEmail -> {
                        PlisioPaymentSheetUserEmail(paymentStep)
                    }

                    is PlisioPaymentStep.Currency -> {
                        PlisioPaymentSheetCurrency(paymentStep)
                    }

                    is PlisioPaymentStep.Payment -> {
                        PlisioPaymentSheetPayment(paymentStep)
                    }

                    is PlisioPaymentStep.Confirmation -> {
                        PlisioPaymentSheetConfirmation(paymentStep)
                    }

                    is PlisioPaymentStep.Completion -> {
                        PlisioPaymentSheetCompletion(paymentStep)
                    }

                    is PlisioPaymentStep.Error -> {
                        PlisioPaymentSheetError(paymentStep)
                    }

                    is PlisioPaymentStep.Loading -> {}
                }
            }
        }
        Spacer(Modifier.weight(1f))
    }
}