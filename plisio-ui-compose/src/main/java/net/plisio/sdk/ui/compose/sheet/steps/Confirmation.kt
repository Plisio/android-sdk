package net.plisio.sdk.ui.compose.sheet.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import net.plisio.sdk.ui.compose.R
import net.plisio.sdk.ui.compose.style.PlisioTextStyle
import net.plisio.sdk.uimodels.PlisioPaymentStep

@Composable
fun PlisioPaymentSheetConfirmation(
    paymentStep: PlisioPaymentStep.Confirmation,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .size(64.dp)
        ) {
            CircularProgressIndicator()
        }
        Text(
            text = stringResource(R.string.plisio_payment_sheet_confirmation_title),
            style = PlisioTextStyle.paymentSheetTitle
        )
        Text(
            text = stringResource(R.string.plisio_payment_sheet_confirmation_text),
            style = PlisioTextStyle.paymentSheetText,
            color = LocalContentColor.current.copy(alpha = 0.5f)
        )
    }
}