package net.plisio.sdk.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import net.plisio.sdk.models.PlisioCryptoCurrency
import net.plisio.sdk.models.PlisioError
import net.plisio.sdk.models.PlisioInvoice
import net.plisio.sdk.models.PlisioInvoiceID
import net.plisio.sdk.models.PlisioShop

@Serializable
data class PlisioInvoiceDetails(
    @SerialName("invoice") val invoice: PlisioInvoice,
    @SerialName("shop") val shop: PlisioShop,
    @SerialName("paysys") val currency: PlisioCryptoCurrency,
    @SerialName("allowed_psys_cids") val availableCurrencies: List<PlisioCryptoCurrency>,
    @SerialName("active_invoice_id") val activeInvoiceID: PlisioInvoiceID? = null
) {
    val canChangeCurrency: Boolean
        get() = availableCurrencies.size > 1 && invoice.statusCode != PlisioInvoice.Status.PartialPayment
}

@Serializable
internal data class InvoiceResponse(
    @SerialName("status") val status: Status,
    @SerialName("data") val invoiceDetails: PlisioInvoiceDetails
)

@Serializable
internal data class ErrorResponse(
    @SerialName("status") val status: Status,
    @SerialName("data") val error: PlisioError
)

@Serializable
enum class Status {
    @SerialName("success")
    Success,
    @SerialName("error")
    Error
}