package net.plisio.sdk.ui.compose.sheet.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.plisio.sdk.models.PlisioErrorWithResponseText
import net.plisio.sdk.ui.compose.R
import net.plisio.sdk.ui.compose.components.PlisioTextButton
import net.plisio.sdk.ui.compose.copyToClipboard
import net.plisio.sdk.ui.compose.style.PlisioTextStyle
import net.plisio.sdk.uimodels.PlisioPaymentStep

@Composable
fun PlisioPaymentSheetError(
    paymentStep: PlisioPaymentStep.Error,
    modifier: Modifier = Modifier
) {
    val error = paymentStep.error
    val errorMessage = error.message ?: error.toString()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(R.string.plisio_payment_sheet_error_title_message),
                style = PlisioTextStyle.paymentSheetTitle,
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
            PlisioTextButton(
                text = stringResource(R.string.plisio_payment_sheet_error_action_copy),
                icon = painterResource(R.drawable.plisio_icon_copy),
                onClick = copyToClipboard(R.string.plisio_payment_sheet_error_toast_copied_message) { errorMessage },
                contentPadding = PaddingValues(horizontal = 8.dp)
            )
        }
        Text(
            text = errorMessage,
            style = PlisioTextStyle.paymentSheetText,
            fontSize = 14.sp,
            textAlign = TextAlign.Start,
            color = LocalContentColor.current,
            maxLines = 15,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
        )
        if (error is PlisioErrorWithResponseText) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.plisio_payment_sheet_error_title_response),
                    style = PlisioTextStyle.paymentSheetTitle,
                    textAlign = TextAlign.Start,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )
                PlisioTextButton(
                    text = stringResource(R.string.plisio_payment_sheet_error_action_copy),
                    icon = painterResource(R.drawable.plisio_icon_copy),
                    onClick = copyToClipboard(R.string.plisio_payment_sheet_error_toast_copied_response) { error.responseText },
                    contentPadding = PaddingValues(horizontal = 8.dp)
                )
            }
            Text(
                text = error.responseText,
                style = PlisioTextStyle.paymentSheetText,
                fontSize = 14.sp,
                textAlign = TextAlign.Start,
                color = LocalContentColor.current,
                maxLines = 10,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .fillMaxWidth()
            )
        }
    }
}