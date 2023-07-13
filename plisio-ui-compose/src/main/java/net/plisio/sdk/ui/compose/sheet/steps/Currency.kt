package net.plisio.sdk.ui.compose.sheet.steps

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.LocalContentColor
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import net.plisio.sdk.models.PlisioCryptoCurrency
import net.plisio.sdk.ui.compose.R
import net.plisio.sdk.ui.compose.components.LocalImageLoader
import net.plisio.sdk.ui.compose.style.PlisioTextStyle
import net.plisio.sdk.uimodels.PlisioPaymentStep

@Composable
fun PlisioPaymentSheetCurrency(
    paymentStep: PlisioPaymentStep.Currency,
    modifier: Modifier = Modifier
) {
    val columns = 2
    val currencies = remember(paymentStep.currencies, columns) { paymentStep.currencies.chunked(columns) }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = stringResource(R.string.plisio_payment_sheet_currency_title),
            style = PlisioTextStyle.paymentSheetTitle
        )
        Text(
            text = stringResource(R.string.plisio_payment_sheet_currency_text),
            style = PlisioTextStyle.paymentSheetText,
            color = LocalContentColor.current.copy(alpha = 0.5f)
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp)
        ) {
            currencies.forEachIndexed { row, rowCurrencies ->
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    rowCurrencies.forEach { currency ->
                        PlisioCryptoCurrencyCard(
                            currency = currency,
                            onClick = {
                                paymentStep.setCurrency(currency = currency.id)
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }
                    if (row > 0 && rowCurrencies.size < columns) {
                        Spacer(Modifier.weight((columns - rowCurrencies.size).toFloat()))
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterialApi::class)
fun PlisioCryptoCurrencyCard(
    currency: PlisioCryptoCurrency,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        backgroundColor = LocalContentColor.current.copy(alpha = 0.05f),
        elevation = 0.dp,
        enabled = currency.isEnabled,
        modifier = modifier
            .defaultMinSize(minHeight = 56.dp)
            .alpha(if (currency.isEnabled) 1f else 0.5f)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(8.dp)
        ) {
            currency.iconURL?.let { iconURL ->
                AsyncImage(
                    model = iconURL,
                    contentDescription = null,
                    imageLoader = LocalImageLoader.current,
                    modifier = Modifier.size(24.dp)
                )
            }
            Column {
                Text(
                    text = currency.name,
                    style = PlisioTextStyle.currencyCardName,
                    color = LocalContentColor.current,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = currency.formattedAmount,
                    style = PlisioTextStyle.currencyCardAmount,
                    color = LocalContentColor.current.copy(alpha = 0.5f),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
