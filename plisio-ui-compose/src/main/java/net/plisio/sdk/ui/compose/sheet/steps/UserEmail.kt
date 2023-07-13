package net.plisio.sdk.ui.compose.sheet.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import net.plisio.sdk.ui.compose.R
import net.plisio.sdk.ui.compose.components.LocalAdaptiveSheetLayoutState
import net.plisio.sdk.ui.compose.components.PlisioButton
import net.plisio.sdk.ui.compose.components.PlisioTextField
import net.plisio.sdk.ui.compose.components.TextFieldValueSaver
import net.plisio.sdk.ui.compose.style.PlisioTextStyle
import net.plisio.sdk.uimodels.PlisioPaymentStep

@Composable
@OptIn(ExperimentalComposeUiApi::class)
fun PlisioPaymentSheetUserEmail(
    paymentStep: PlisioPaymentStep.UserEmail,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable(stateSaver = TextFieldValueSaver) { mutableStateOf(TextFieldValue()) }
    val emailFocusRequester = remember { FocusRequester() }

    val isEmailValid = '@' in email.text
    val setUserEmail = {
        if (isEmailValid) {
            paymentStep.setUserEmail(email.text)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.plisio_icon_email),
            tint = MaterialTheme.colors.primary,
            contentDescription = null,
            modifier = Modifier
                .padding(bottom = 12.dp)
                .size(64.dp)
        )
        Text(
            text = stringResource(R.string.plisio_payment_sheet_email_title),
            style = PlisioTextStyle.paymentSheetTitle
        )
        Text(
            text = stringResource(R.string.plisio_payment_sheet_email_text),
            style = PlisioTextStyle.paymentSheetText,
            color = LocalContentColor.current.copy(alpha = 0.5f)
        )
        PlisioTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = stringResource(R.string.plisio_payment_sheet_email_placeholder),
            icon = painterResource(R.drawable.plisio_icon_email),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                autoCorrect = false,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions { setUserEmail() },
            focusRequester = emailFocusRequester,
            modifier = Modifier.padding(top = 12.dp)
        )
        AnimatedVisibility(visible = paymentStep.error != null) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.plisio_icon_error),
                    contentDescription = null,
                    tint = MaterialTheme.colors.error,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = paymentStep.error.orEmpty(),
                    style = PlisioTextStyle.paymentSheetError,
                    color = MaterialTheme.colors.error,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        PlisioButton(
            text = stringResource(R.string.plisio_payment_sheet_action_continue),
            onClick = setUserEmail,
            enabled = isEmailValid,
            modifier = Modifier.padding(top = 12.dp)
        )
    }

    val isSheetVisible = LocalAdaptiveSheetLayoutState.current.isVisible
    val softwareKeyboardController = LocalSoftwareKeyboardController.current
    LaunchedEffect(isSheetVisible) {
        if (isSheetVisible) {
            emailFocusRequester.requestFocus()
            softwareKeyboardController?.show()
        }
    }
}