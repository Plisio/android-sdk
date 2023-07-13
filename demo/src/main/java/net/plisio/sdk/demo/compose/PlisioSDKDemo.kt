package net.plisio.sdk.demo.compose

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import net.plisio.sdk.ui.compose.PlisioPaymentButton
import net.plisio.sdk.ui.compose.PlisioPaymentSheet
import net.plisio.sdk.ui.compose.plisioPaymentViewModel
import net.plisio.sdk.ui.compose.style.LocalPlisioStyle
import net.plisio.sdk.ui.compose.style.PlisioColor
import net.plisio.sdk.ui.compose.style.PlisioStyle
import net.plisio.sdk.ui.compose.style.contrastingColorFor
import kotlin.random.Random

@Composable
fun PlisioSDKDemo() {
    var invoiceID by remember { mutableStateOf(TextFieldValue("")) }
    var invoiceViewKey by remember { mutableStateOf(TextFieldValue("")) }

    val paymentViewModel = plisioPaymentViewModel()
    val paymentStep by paymentViewModel.paymentStep.collectAsState()

    var paymentButtonStyle by rememberSaveable { mutableStateOf(0) }

    var paymentSheetHeaderStyle by rememberSaveable { mutableStateOf(0) }
    var showPaymentSheetHeader by rememberSaveable { mutableStateOf(true) }

    var isPaymentSheetVisible by rememberSaveable { mutableStateOf(false) }

    var randomAccentColor by remember { mutableStateOf(Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())) }
    val themeAccentColor = if (paymentSheetHeaderStyle == 1) randomAccentColor else PlisioColor.primary

    MaterialTheme(
        colors = when (isSystemInDarkTheme()) {
            true -> darkColors(
                primary = themeAccentColor,
                onPrimary = contrastingColorFor(themeAccentColor),
                surface = PlisioColor.dark,
                onSurface = PlisioColor.light,
                error = PlisioColor.error,
                onError = PlisioColor.light
            )

            false -> lightColors(
                primary = themeAccentColor,
                onPrimary = contrastingColorFor(themeAccentColor),
                surface = PlisioColor.light,
                onSurface = PlisioColor.dark,
                error = PlisioColor.error,
                onError = PlisioColor.light
            )
        }
    )
    {
        CompositionLocalProvider(
            LocalPlisioStyle provides PlisioStyle.fromTheme().copy(
                paymentSheetHeader = when (paymentSheetHeaderStyle) {
                    0 -> PlisioStyle.PaymentSheetHeader.Gradient()
                    1 -> PlisioStyle.PaymentSheetHeader.Colored(themeAccentColor)
                    else -> PlisioStyle.PaymentSheetHeader.Transparent
                },
                showPaymentSheetHeader = showPaymentSheetHeader
            )
        ) {
            Surface {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .verticalScroll(rememberScrollState())
                        .systemBarsPadding()
                        .imePadding()
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    TextField(
                        value = invoiceID,
                        onValueChange = { invoiceID = it },
                        label = { Text("Invoice ID") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Next,
                            autoCorrect = false
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    TextField(
                        value = invoiceViewKey,
                        onValueChange = { invoiceViewKey = it },
                        label = { Text("Invoice view key") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            autoCorrect = false
                        ),
                        keyboardActions = KeyboardActions {
                            paymentViewModel.loadInvoice(
                                id = invoiceID.text,
                                viewKey = invoiceViewKey.text
                            )
                            isPaymentSheetVisible = true
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    PlisioPaymentButton(
                        paymentStep = paymentStep,
                        style = PlisioStyle.PaymentButton.values()[paymentButtonStyle],
                        onClick = {
                            paymentViewModel.loadInvoice(
                                id = invoiceID.text,
                                viewKey = invoiceViewKey.text
                            )
                            isPaymentSheetVisible = true
                        },
                        shape = RoundedCornerShape(8.dp),
                        indicatorShape = RoundedCornerShape(4.dp)
                    )

                    Divider()

                    Text("PlisioPaymentButton")

                    ButtonGroup(
                        items = PlisioStyle.PaymentButton.values().map { it.name },
                        selectedIndex = paymentButtonStyle,
                        onItemSelected = { paymentButtonStyle = it },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Divider()

                    Text("PlisioPaymentSheet")

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        CheckButton(
                            checked = showPaymentSheetHeader,
                            onCheckedChange = { showPaymentSheetHeader = it },
                            text = "Header"
                        )
                        ButtonGroup(
                            items = listOf("Gradient", "Color", "Transparent"),
                            selectedIndex = paymentSheetHeaderStyle,
                            onItemSelected = {
                                paymentSheetHeaderStyle = it
                                randomAccentColor = Color(Random.nextFloat(), Random.nextFloat(), Random.nextFloat())
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                PlisioPaymentSheet(
                    isVisible = isPaymentSheetVisible,
                    setVisibility = { isPaymentSheetVisible = it },
                    paymentStep = paymentStep,
                    shape = RoundedCornerShape(8.dp)
                )
            }
        }
    }
}